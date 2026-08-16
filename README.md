# **Cantina Simulator 2026** 

## **Descripción** 

"Cantina Simulator 2026" es un videojuego 2D de gestión y simulación ambientado en la cantina de una escuela técnica. El jugador deberá administrar recursos, atender clientes, resolver situaciones cotidianas y tomar decisiones que afectarán el desarrollo de la partida, combinando elementos de los géneros cozy game, tycoon y simulación económica. 

## **Integrantes** 

Theo Gonzalez Luca Sprovieri 

## **Tecnologías Utilizadas** 

**Lenguaje:** Java 21 LTS 

**Framework:** libGDX 1.14.2 

**Herramienta de construcción:** Gradle 9.6.1 

**IDE utilizado:** IntelliJ IDEA 

**Plataforma objetivo:** Desktop (LWJGL3) 

**Base de Datos (Planificada):** SQLite (mediante JDBC) 

## **Cómo Ejecutar el Proyecto** 

### **Requisitos previos:** 

JDK 21 instalado. 

Git. 

### **Clonación del repositorio e ingreso a la carpeta:** 

Abrir la terminal o Git Bash. 

Clonar el repositorio ejecutando: 

git clone https://github.com/LucaSpro401/Cantina-Simulator-2026.git 

Ingresar a la carpeta del proyecto: 

cd Cantina-Simulator-2026 

### **Comandos de ejecución:** 

#### **En Windows:** 

gradlew.bat lwjgl3:run 

#### **En Linux / macOS:** 

./gradlew lwjgl3:run 

## **Base de Datos** 

El proyecto utilizará una base de datos relacional SQLite almacenada de forma local en el directorio del videojuego, conectada a través de JDBC para la persistencia del progreso y estadísticas. 

**Motor elegido:** SQLite (local / embedded) 

**Conectividad:** JDBC (Java Database Connectivity) 

### **Tablas y campos principales:** 

#### **Partida** 

(id_partida [PK], nombre_jugador, dia_actual, dinero_disponible, nivel_hambre, infracciones_recibidas, fecha_creacion) 

#### **Producto** 

(id_producto [PK], nombre, precio_compra, precio_venta, categoria) 

#### **Stock** 

(id_stock [PK], id_partida [FK], id_producto [FK], cantidad_disponible) 

#### **Venta** 

(id_venta [PK], id_partida [FK], id_producto [FK], cantidad, importe_total, fecha) 

#### **Estadística** 

(id_estadistica [PK], id_partida [FK], clientes_atendidos, productos_vendidos, ingresos_obtenidos, mejor_ganancia, dias_sobrevividos) 

#### **Configuración** 

(id_configuracion [PK], id_partida [FK], volumen_musica, opciones_generales) 

### **Claves y Relaciones:** 

Partida (1) a (N) Stock 

Partida (1) a (N) Venta 

Producto (1) a (N) Stock 

Producto (1) a (N) Venta 

Partida (1) a (1) Estadística 

Partida (1) a (1) Configuración 

### **Consultas necesarias:** 

**INSERT:** Creación de nueva partida, registro de ventas, guardado de preferencias y almacenamiento de estadísticas finales. 

**SELECT:** Carga de estado de partida, consulta de catálogo de productos, control de stock disponible y lectura de configuraciones. 

**UPDATE:** Actualización continua de dinero, nivel de hambre, día actual, cantidad de infracciones y reabastecimiento de stock. 

**DELETE:** Reinicio de progreso o eliminación de partidas guardadas. 

## **Estado Actual del Proyecto** 

En desarrollo. 

## **Enlace a la Wiki del Proyecto** 
[https://github.com/LucaSpro401/Cantina-Simulator-2026/wiki/INFORMACION
](https://github.com/LucaSpro401/Cantina-Simulator-2026/wiki/INFORMACION)
