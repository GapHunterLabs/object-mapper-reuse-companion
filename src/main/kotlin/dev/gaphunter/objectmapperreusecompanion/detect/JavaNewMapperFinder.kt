package dev.gaphunter.objectmapperreusecompanion.detect

import com.intellij.psi.JavaRecursiveElementWalkingVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNewExpression
import com.intellij.psi.util.PsiTreeUtil
import dev.gaphunter.objectmapperreusecompanion.model.NewMapperHit

/**
 * Finds `new ObjectMapper()` (or `XmlMapper`/`YAMLMapper`/etc.,
 * [JacksonMapperSignals]) constructions written inside a
 * non-constructor method body -- Jackson's own javadoc states
 * `ObjectMapper` is "meant to be used as singleton across the lifetime
 * of the application... also very expensive to create". Building one
 * inside a regular method means the real construction/configuration
 * cost is paid on every call.
 *
 * **v0.1 scope, stated honestly:** never flags a construction inside a
 * constructor or a field initializer (legitimate "create once"
 * locations). Doesn't trace a mapper built once and passed around --
 * only the construction site itself is checked.
 */
object JavaNewMapperFinder {

    fun findAll(file: PsiFile): List<NewMapperHit> {
        val hits = mutableListOf<NewMapperHit>()
        file.accept(object : JavaRecursiveElementWalkingVisitor() {
            override fun visitNewExpression(expression: PsiNewExpression) {
                super.visitNewExpression(expression)
                hitFor(expression)?.let { hits += it }
            }
        })
        return hits
    }

    private fun hitFor(newExpr: PsiNewExpression): NewMapperHit? {
        val className = newExpr.classReference?.referenceName ?: return null
        if (className !in JacksonMapperSignals.MAPPER_CLASS_NAMES) return null

        val containingMethod = PsiTreeUtil.getParentOfType(newExpr, PsiMethod::class.java) ?: return null
        if (containingMethod.isConstructor) return null

        return NewMapperHit(leafOf(newExpr), className)
    }

    private fun leafOf(element: PsiElement): PsiElement {
        var current = element
        while (current.firstChild != null) current = current.firstChild
        return current
    }
}
