package dev.gaphunter.objectmapperreusecompanion.detect

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import dev.gaphunter.objectmapperreusecompanion.model.NewMapperHit
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtConstructor
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

/** Kotlin counterpart of [JavaNewMapperFinder] -- `ObjectMapper()` (no `new` keyword in Kotlin). */
object KotlinNewMapperFinder {

    fun findAll(file: PsiFile): List<NewMapperHit> {
        if (file !is KtFile) return emptyList()
        val hits = mutableListOf<NewMapperHit>()
        file.accept(object : KtTreeVisitorVoid() {
            override fun visitCallExpression(expression: KtCallExpression) {
                super.visitCallExpression(expression)
                hitFor(expression)?.let { hits += it }
            }
        })
        return hits
    }

    private fun hitFor(call: KtCallExpression): NewMapperHit? {
        val className = call.calleeExpression?.text ?: return null
        if (className !in JacksonMapperSignals.MAPPER_CLASS_NAMES) return null

        if (PsiTreeUtil.getParentOfType(call, KtConstructor::class.java) != null) return null
        if (PsiTreeUtil.getParentOfType(call, KtNamedFunction::class.java) == null) return null

        return NewMapperHit(leafOf(call), className)
    }

    private fun leafOf(element: PsiElement): PsiElement {
        var current = element
        while (current.firstChild != null) current = current.firstChild
        return current
    }
}
