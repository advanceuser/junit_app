package com.capgemini.models;

import com.capgemini.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest() {
        System.out.println("Iniciando el metodo");
        this.cuenta = new Cuenta("Mariana Sarabia", new BigDecimal("1000.12345"));

    }

    @AfterEach
    void ending() {
        System.out.println("Finalizando prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }

    @Test
    @DisplayName("Probando el nombre de la cuenta")
    void testNombreCuenta() {
        String esperado = "Mariana Sarabia";
        String real = cuenta.getPersona();
        assertNotNull(real, () -> "La cuenta no puede ser nula");
        assertEquals(esperado, real, () -> "El nombre de la cuenta no es el esperado: se esperaba " + esperado + " sin embargo fue: " + real);
        assertTrue(real.equals("Mariana Sarabia"), () -> "Nombre cuenta esperada debe ser igual a la real");
    }

    @Test
    @DisplayName("Probando el saldo de la cuenta corriente , que no sea null, mayor que cero, valor esperado")
    void testSaldoCuenta() {
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Testeado referencias que sean igual con el metodo equals")
    void testReferenciaCuenta() {
        cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        //assertNotEquals(cuenta2,cuenta1);
        assertEquals(cuenta2, cuenta);
    }

    @Test
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {

        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteException() {
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void transferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Test
    //@Disabled
    @DisplayName("Probando reaciones entre las cuentas y el banco con assertAll")
    void testRelacionBancoCuentas() {
        //fail();
        Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertAll(() -> {
            assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        }, () -> {
            assertEquals("3000", cuenta1.getSaldo().toPlainString());
        }, () -> {
            assertEquals(2, banco.getCuentas().size());
        }, () -> {
            assertEquals("Banco del Estado", cuenta1.getBanco().getNombre());
        }, () -> {
            assertEquals("Andres", banco.getCuentas().stream()
                    .filter(c -> c.getPersona().equals("Andres"))
                    .findFirst().get().getPersona());
        }, () -> {
            assertTrue(banco.getCuentas().stream()
                    .anyMatch(c -> c.getPersona().equals("Andres")));
        });
    }

    @Nested
    @DisplayName("Inner Class Testing SO Variables")
    class testingSOVariables {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
            System.out.println("Ejecucion completa en este metodo de prueba");
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {

        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {

        }

        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void testSoloJDK8() {

        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void testSoloJDK17() {

        }

        @Test
        @DisabledOnJre(JRE.JAVA_17)
        void testNoJDK17() {
        }
    }

    @Test
    void imprimirSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> System.out.println(k + " : " + v));
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = "17.0.9")
    void testJavaVersion() {

    }

    @Test
    @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
    void testSolo64() {

    }

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
    void testSolo32() {

    }

    @Test
    @EnabledIfSystemProperty(named = "user.name", matches = "lreynoso")
    void testUserName() {

    }

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void testDesarrollo() {

    }

    @Test
    void testImprimirVariablesAmbiente() {
        Map<String, String> getenv = System.getenv();
        getenv.forEach((k, v) -> System.out.println(k + " : " + v));

    }

    @Nested
    @DisplayName("Inner Class for System Properties")
    class SystemProperties {
        @Test
        @EnabledIfEnvironmentVariable(named = "USERDNSDOMAIN", matches = "CORP.CAPGEMINI.COM")
        void testJavaHome() {

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
        void testProcesadores() {

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {

        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProduction() {

        }

    }

    @Test
    @DisplayName("testSaldoCuentaDev")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        //assumeTrue(esDev);
        assumingThat(esDev, () -> {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        });

    }

    @Test
    @DisplayName("testSaldoCuentaDev_2")
    void testSaldoCuentaDev2() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        System.out.println(esDev);
        assumingThat(esDev, () -> {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        });

    }
}