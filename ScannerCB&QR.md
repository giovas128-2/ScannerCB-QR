# 📱 SCANNER CB&QR
<img width="200" alt="ScannerCB QR_Logo" src="https://github.com/user-attachments/assets/983e133b-390e-474b-8959-8bbd2cbf5015" />

Aplicación móvil para **escaneo de códigos QR y de barras**, que permite obtener información rápida sobre enlaces o productos y guardarlos en un historial.

---

## 📝 Análisis de Requisitos

### Funciones principales

**Escanear código QR**
- Mostrar el enlace detectado  
- Opciones: abrir enlace o copiarlo  

**Escanear código de barras**
- Mostrar imagen, nombre y descripción usando la **API UPCItemDB**  
- Opciones: buscar producto en **Mercado Libre**, **Amazon** o **Google**  

**Historial de escaneos**
- Guardar todo lo escaneado por el usuario para revisarlo posteriormente  

**Usuarios objetivo:** personas que quieren información rápida sobre productos o enlaces  

### Capturas del proceso
<div style="display: flex; flex-wrap: wrap; gap: 10px;">
  <img src="https://github.com/user-attachments/assets/109ba4ee-8671-4927-9456-65f0c64101fe" width="250" alt="Escaneo principal" />
  <img src="https://github.com/user-attachments/assets/c055438d-c3ca-4fcc-9ffe-3e060d961f3f" width="250" alt="Resultado QR" />
  <img src="https://github.com/user-attachments/assets/dcef1c22-8663-41ef-8bbd-88334d2b28e8" width="250" alt="Resultado Código de Barras" />
  <img src="https://github.com/user-attachments/assets/ea939495-4132-4b1a-9f79-8d99b0edf4c1" width="250" alt="Historial" />
</div>

---

## 🎨 Diseño

**Flujo de navegación:**
Pantalla principal → Escanear → Detecta tipo (QR o código de barras)
→ QR → mostrar enlace → acción (abrir/copiar)
→ Código de barras → mostrar info → opciones de búsqueda
Pantalla principal → Historial → ver todos los escaneos


---

## 💻 Desarrollo

**Lenguajes y herramientas:**
- Kotlin en **Android Studio**  
- Librería de escaneo: **ZXing** o **ML Kit Barcode Scanner**  
- API **UPCItemDB**  
- **DataStore** o **Room** para historial  

**Estructura de módulos:**
- `MainActivity` → pantalla principal y historial  
- `ScannerActivity` → manejo del escaneo  
- `ResultActivity` → mostrar resultados  
- `ProductRepository` → interacción con API  
- `HistoryRepository` → guardar y recuperar historial  

---

## 🧪 Pruebas
- **Unitarias:** verificar historial  
- **Integradas:** escanear → mostrar datos → abrir opciones  
- **Con usuario:** botones abrir, copiar, buscar  

---

## 📦 Entrega
- Código organizado y comentado  
- Documentación: capturas, flujo de uso, descripción de API y librerías  

---

## ⚙️ Mantenimiento
- Categorías o filtros en historial  
- Preferencias del usuario  
- Mejorar diseño visual (temas, animaciones)  
