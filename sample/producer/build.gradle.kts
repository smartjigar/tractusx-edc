plugins {
    `java-library`
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}


dependencies {

    runtimeOnly(project(":edc-controlplane:edc-controlplane-postgresql-hashicorp-vault"))
    runtimeOnly(libs.bundles.edc.sqlstores)
    runtimeOnly(libs.edc.transaction.local)
    runtimeOnly(libs.edc.sql.pool)
    runtimeOnly(libs.edc.core.controlplane)
    runtimeOnly(libs.edc.dpf.transfer)
    runtimeOnly(libs.postgres)


    implementation(project(":edc-dataplane:edc-dataplane-base"))
    runtimeOnly(libs.edc.transaction.local)
    runtimeOnly(libs.edc.sql.pool)
    runtimeOnly(libs.postgres)



    api(project(":edc-extensions:bpn-validation:bpn-validation-spi"))
    api(project(":edc-extensions:bpn-validation:bpn-validation-api"))
    api(project(":edc-extensions:bpn-validation:bpn-validation-core"))
    implementation(project(":edc-extensions:cx-oauth2"))
    implementation(project(":edc-extensions:data-encryption"))
    implementation(project(":edc-extensions:dataplane-selector-configuration"))
    implementation(project(":edc-extensions:postgresql-migration"))
    implementation(project(":edc-extensions:provision-additional-headers"))
    /*implementation(project(":edc-extensions:transferprocess-sftp-client"))
    implementation(project(":edc-extensions:transferprocess-sftp-common"))
    implementation(project(":edc-extensions:transferprocess-sftp-provisioner"))*/
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    exclude("**/pom.properties", "**/pom.xm")


    mergeServiceFiles()
    archiveFileName.set("producer.jar")
}

application {
    mainClass.set("org.eclipse.edc.boot.system.runtime.BaseRuntime")
}
