package dev.gaphunter.objectmapperreusecompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JavaNewMapperFinderTest : BasePlatformTestCase() {

    fun `test a mapper built inside a regular method is flagged`() {
        val file = myFixture.configureByText(
            "OrderService.java",
            """
            class OrderService {
                String serialize() {
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.writeValueAsString(this);
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, JavaNewMapperFinder.findAll(file).size)
    }

    fun `test a mapper built inside a constructor is not flagged`() {
        val file = myFixture.configureByText(
            "OrderService.java",
            """
            class OrderService {
                private final ObjectMapper mapper;
                OrderService() {
                    mapper = new ObjectMapper();
                }
            }
            """.trimIndent(),
        )
        assertTrue(JavaNewMapperFinder.findAll(file).isEmpty())
    }

    fun `test an unrelated new expression is never flagged`() {
        val file = myFixture.configureByText(
            "OrderService.java",
            """
            class OrderService {
                void process() {
                    StringBuilder sb = new StringBuilder();
                }
            }
            """.trimIndent(),
        )
        assertTrue(JavaNewMapperFinder.findAll(file).isEmpty())
    }
}
