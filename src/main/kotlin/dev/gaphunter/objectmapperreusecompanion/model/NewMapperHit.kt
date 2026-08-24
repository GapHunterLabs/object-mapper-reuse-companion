package dev.gaphunter.objectmapperreusecompanion.model

import com.intellij.psi.PsiElement

/** One `new ObjectMapper()` (or `XmlMapper`/`YAMLMapper`) construction found inside a non-constructor method body. */
data class NewMapperHit(val callElement: PsiElement, val mapperClassName: String)
