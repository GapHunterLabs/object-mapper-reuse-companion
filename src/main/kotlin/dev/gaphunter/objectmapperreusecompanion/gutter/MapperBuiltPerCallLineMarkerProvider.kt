package dev.gaphunter.objectmapperreusecompanion.gutter

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import dev.gaphunter.objectmapperreusecompanion.detect.JavaNewMapperFinder
import dev.gaphunter.objectmapperreusecompanion.detect.KotlinNewMapperFinder
import dev.gaphunter.objectmapperreusecompanion.model.NewMapperHit
import dev.gaphunter.objectmapperreusecompanion.review.ReviewPrompt

class MapperBuiltPerCallLineMarkerProvider : LineMarkerProviderDescriptor(), DumbAware {

    override fun getName(): String = "Jackson mapper built inside a method"

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>, result: MutableCollection<in LineMarkerInfo<*>>) {
        val file = elements.firstOrNull()?.containingFile ?: return
        val hits = when (file.language.id) {
            "JAVA" -> JavaNewMapperFinder.findAll(file)
            "kotlin" -> KotlinNewMapperFinder.findAll(file)
            else -> emptyList()
        }
        if (hits.isEmpty()) return

        val hitsByElement = hits.associateBy { it.callElement }
        for (element in elements) {
            val hit = hitsByElement[element] ?: continue
            result.add(buildMarker(hit))

            val path = file.virtualFile?.path ?: continue
            val lineNumber = file.viewProvider.document?.getLineNumber(element.textRange.startOffset) ?: -1
            ReviewPrompt.recordHit(file.project, "$path:$lineNumber")
        }
    }

    private fun buildMarker(hit: NewMapperHit): LineMarkerInfo<PsiElement> {
        val tooltip = "${hit.mapperClassName} is built here inside a method -- Jackson's own javadoc says it's " +
            "\"meant to be used as singleton... also very expensive to create\""
        return LineMarkerInfo(
            hit.callElement,
            hit.callElement.textRange,
            MapperReuseIcons.RISK,
            { _: PsiElement -> tooltip },
            null,
            GutterIconRenderer.Alignment.RIGHT,
            { tooltip },
        )
    }
}
