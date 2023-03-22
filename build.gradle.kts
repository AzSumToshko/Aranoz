plugins {
	java
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_19

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")//hibernate
	implementation("org.springframework.boot:spring-boot-starter-hateoas")//otgovarq za ModelAndView
	implementation("org.springframework.boot:spring-boot-starter-security:2.6.14")
	implementation("org.springframework.boot:spring-boot-starter-web:2.6.14")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")//otgovarq za implementaciqta na cikli i tn v HTML-a
	implementation("org.springframework.boot:spring-boot-starter-validation:3.0.2")//validaciqta
	implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")//convert na obekt na string


	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
	implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.2.0")//otgovarq za layout page-a

	implementation("mysql:mysql-connector-java:8.0.32")//vruzva bazata danni
	implementation("org.modelmapper:modelmapper:3.1.1")//mapi ot VM na entity

	implementation("javax.mail:mail:1.5.0-b01")
    testImplementation("junit:junit:4.13.1")
	testImplementation("junit:junit:4.13.1")//upravlqva mailservice-a


	compileOnly("org.projectlombok:lombok")//anotaciite za geteri seteri i tn.
	annotationProcessor("org.projectlombok:lombok")//gornoto cheti
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
