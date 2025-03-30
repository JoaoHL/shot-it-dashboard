package br.com.acgj.shotit

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(InfraContainersForTestConfiguration::class)
class ShotitApplicationTests {

	@Test
	fun contextLoads() {
	}

}
