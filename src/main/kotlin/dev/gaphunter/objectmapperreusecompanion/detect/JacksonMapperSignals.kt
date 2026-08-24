package dev.gaphunter.objectmapperreusecompanion.detect

/**
 * Jackson mapper class names this plugin recognizes -- matched by
 * simple name only, so it works whether the real Jackson jar is on
 * the classpath or not. `ObjectMapper` and its common format-specific
 * subclasses all share the same documented "expensive to create,
 * thread-safe, meant to be a singleton" characteristics (Jackson's own
 * javadoc: "meant to be used as singleton across the lifetime of the
 * application... also very expensive to create").
 */
object JacksonMapperSignals {
    val MAPPER_CLASS_NAMES = setOf("ObjectMapper", "XmlMapper", "YAMLMapper", "CBORMapper", "CsvMapper")
}
