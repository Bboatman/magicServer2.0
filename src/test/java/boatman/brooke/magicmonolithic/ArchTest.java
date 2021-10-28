package boatman.brooke.magicmonolithic;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("boatman.brooke.magicmonolithic");

        noClasses()
            .that()
            .resideInAnyPackage("boatman.brooke.magicmonolithic.service..")
            .or()
            .resideInAnyPackage("boatman.brooke.magicmonolithic.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..boatman.brooke.magicmonolithic.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
