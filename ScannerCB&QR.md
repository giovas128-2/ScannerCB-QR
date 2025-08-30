# 📱 SCANNER CB&QR
<img width="274" height="217" alt="ScannerCB QR_Logo" src="https://github.com/user-attachments/assets/983e133b-390e-474b-8959-8bbd2cbf5015" />

Aplicación móvil para **escaneo de códigos QR y de barras**, que permite obtener información rápida sobre enlaces o productos y guardarlos en un historial.

---

## 📝 Análisis de Requisitos
<img src="https://github.com/user-attachments/assets/109ba4ee-8671-4927-9456-65f0c64101fe" width="300" alt="ScannerCByQR_Escaneo_principal" />

### Funciones principales
1. **Escanear código QR**  
   - Mostrar el enlace detectado.  
   - Opciones: abrir enlace o copiarlo.  
<img src="https://github.com/user-attachments/assets/c055438d-c3ca-4fcc-9ffe-3e060d961f3f" width="300" alt="ScannerCByQR_ResultQR" />

2. **Escanear código de barras**  
   - Mostrar imagen, nombre y descripción del producto usando la **API UPCItemDB**.  
   - Opciones: buscar producto en **Mercado Libre**, **Amazon** o **Google**.  
<img src="https://github.com/user-attachments/assets/dcef1c22-8663-41ef-8bbd-88334d2b28e8" width="300" alt="ScannerCByQR_ResultCB" />

3. **Historial de escaneos**  
   - Guardar todo lo escaneado por el usuario para revisarlo posteriormente.  
<img src="https://github.com/user-attachments/assets/ea939495-4132-4b1a-9f79-8d99b0edf4c1" width="300" alt="ScannerCByQR_Historial" />

**Usuarios objetivo:** personas que quieren información rápida sobre productos o enlaces.

---

## 🎨 Diseño

### Wireframes básicos
_(Aquí puedes agregar imágenes de tus wireframes si las tienes)_

### Flujo de navegación
Pantalla principal → Escanear → Detecta tipo (QR o código de barras)  
→ QR → mostrar enlace → acción (abrir/copiar)  
→ Código de barras → mostrar info → opciones de búsqueda  
Pantalla principal → Historial → ver todos los escaneos

---

## 💻 Desarrollo

### Lenguajes y herramientas
- Kotlin en **Android Studio**  
- Librería de escaneo de códigos: **ZXing** o **ML Kit Barcode Scanner**  
- API **UPCItemDB** para obtener datos de productos  
- **DataStore** o **Room** para guardar historial  

### Estructura de módulos
- `MainActivity` → pantalla principal y historial  
- `ScannerActivity` → manejo del escaneo  
- `ResultActivity` → mostrar resultados (QR o código de barras)  
- `ProductRepository` → interacción con API UPCItemDB  
- `HistoryRepository` → guardar y recuperar historial  

---

## 🧪 Pruebas

- **Unitarias:** verificar que la app guarde correctamente cada escaneo en el historial  
- **Integradas:** escanear un código de barras → mostrar datos correctos → abrir opciones de búsqueda  
- **Con usuario:** prueba de botones de abrir enlace, copiar y buscar  

---

## 📦 Entrega

- Código organizado y comentado  
- Documentación que incluye:  
  - Capturas de pantalla de la app  
  - Flujo de uso  
  - Descripción de la API y librerías utilizadas  

---

## ⚙️ Mantenimiento

- Agregar categorías o filtros en el historial  
- Guardar preferencias del usuario (por ejemplo, tiendas favoritas para búsqueda)  
- Mejorar diseño visual (temas claros/oscuro, animaciones)  

---
