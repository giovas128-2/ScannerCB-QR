SCANNER CB&QR
________________________________________
1. Análisis de requisitos
•	Funciones principales:
1.	Escanear código QR:
	Mostrar link detectado.
	Opciones: abrir enlace o copiarlo.
2.	Escanear código de barras:
	Mostrar imagen, nombre y descripción del producto usando la API UPCItemDB.
	Opciones: buscar producto en Mercado Libre, Amazon o Google.
3.	Guardar todo lo escaneado en historial del usuario.
•	Usuarios: personas que quieren información rápida sobre productos o enlaces.
________________________________________
2. Diseño
•	Wireframes básicos:
•	Flujo de navegación:
Pantalla principal → Escanear → Detecta tipo (QR o barra)
   → QR → mostrar enlace → acción
   → Código de barras → mostrar info → opciones búsqueda
Pantalla principal → Historial → ver todos los escaneos
________________________________________
3. Desarrollo
•	Lenguaje y herramientas:
o	Kotlin en Android Studio.
o	Librería de escaneo de códigos (ZXing o ML Kit Barcode Scanner).
o	API UPCItemDB para obtener datos de productos.
o	DataStore o Room para guardar historial.
•	Estructura de módulos:
o	MainActivity → pantalla principal, historial.
o	ScannerActivity → manejo del escaneo.
o	ResultActivity → mostrar resultados (QR o código de barras).
o	ProductRepository → interacción con API UPCItemDB.
o	HistoryRepository → guardar y recuperar historial.
________________________________________
4. Pruebas
•	Unitarias: verificar que la app guarde correctamente cada escaneo en el historial.
•	Integradas: escanear un código de barras → mostrar datos correctos → abrir opciones de búsqueda.
•	Con usuario: prueba que los botones de abrir enlace, copiar y buscar funcionen correctamente.
________________________________________
5. Entrega
•	Código organizado y comentado.
•	Documentación con:
o	Capturas de pantalla de la app.
o	Flujo de uso.
o	Descripción de la API y librerías utilizadas.
________________________________________
6. Mantenimiento
•	Agregar categorías o filtros en el historial.
•	Guardar preferencias del usuario (por ejemplo, tiendas favoritas para búsqueda).
•	Mejorar diseño visual (temas claros/oscuro, animaciones).
________________________________________

