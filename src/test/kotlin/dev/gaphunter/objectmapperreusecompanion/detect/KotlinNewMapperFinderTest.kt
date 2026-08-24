package dev.gaphunter.objectmapperreusecompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class KotlinNewMapperFinderTest : BasePlatformTestCase() {

    fun `test a mapper built inside a regular function is flagged`() {
        val file = myFixture.configureByText(
            "OrderService.kt",
            """
            class OrderService {
                fun serialize(): String {
                    val mapper = ObjectMapper()
                    return mapper.writeValueAsString(this)
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, KotlinNewMapperFinder.findAll(file).size)
    }

    fun `test a mapper built as a class property is not flagged`() {
        val file = myFixture.configureByText(
            "OrderService.kt",
            """
            class OrderService {
                val mapper = ObjectMapper()
            }
            """.trimIndent(),
        )
        assertTrue(KotlinNewMapperFinder.findAll(file).isEmpty())
    }

    fun `test an unrelated call expression is never flagged`() {
        val file = myFixture.configureByText(
            "OrderService.kt",
            """
            class OrderService {
                fun process() {
                    val sb = StringBuilder()
                }
            }
            """.trimIndent(),
        )
        assertTrue(KotlinNewMapperFinder.findAll(file).isEmpty())
    }
}
