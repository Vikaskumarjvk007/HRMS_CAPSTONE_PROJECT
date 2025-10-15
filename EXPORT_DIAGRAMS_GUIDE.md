# 📊 How to View and Export UML Diagrams

## ✅ Method 1: View in VS Code (DONE!)

The **Markdown Preview Mermaid Support** extension has been installed in your VS Code!

### How to Use:
1. Open `UML_DIAGRAMS.md` in VS Code
2. Press `Ctrl+Shift+V` (or click the preview icon in top-right)
3. You'll see all diagrams rendered beautifully in the preview pane!

**Tip:** You can keep the editor on the left and preview on the right to see changes live.

---

## 🌐 Method 2: Use Mermaid Live Editor (Online)

### Steps:

1. **Open Mermaid Live:** https://mermaid.live/
   - A browser window has been opened for you!

2. **Copy a Diagram:**
   - Open `UML_DIAGRAMS.md`
   - Find the diagram you want (e.g., Class Diagram)
   - Copy everything between the ` ```mermaid ` and ` ``` ` tags

3. **Paste in Mermaid Live:**
   - Paste the code in the left pane
   - The diagram appears instantly on the right!

4. **Export Options:**
   - Click **"Actions"** menu at top
   - Choose:
     - **Download SVG** (vector, scalable - best for presentations)
     - **Download PNG** (raster image)
     - **Copy SVG to clipboard**
     - **Get shareable link**

### Example - Exporting Class Diagram:

1. Copy this from `UML_DIAGRAMS.md` (lines 7-228):
```mermaid
classDiagram
    %% Model Classes
    class Employee {
        -int id
        -String name
        ...
    }
    ...
```

2. Paste in https://mermaid.live/
3. Click **Actions → Download PNG**
4. Save as `class_diagram.png`

---

## 📁 Method 3: Export All Diagrams as Images for Presentations

### Quick Export Script

Run this PowerShell script to extract all diagrams:

```powershell
# Navigate to project directory
cd C:\Users\akujvika\Desktop\Hrmsproject-1

# Create diagrams export folder
New-Item -ItemType Directory -Force -Path "diagrams_export"

# List of diagrams to export
$diagrams = @(
    "1_class_diagram",
    "2_sequence_login",
    "3_sequence_payroll",
    "4_sequence_leave",
    "5_usecase_diagram",
    "7_erd_diagram",
    "8_activity_diagram",
    "9_state_diagram",
    "10_component_diagram"
)

Write-Host "✅ Diagrams extracted! Follow these steps to export as images:"
Write-Host ""
Write-Host "For each diagram:"
Write-Host "1. Open UML_DIAGRAMS.md"
Write-Host "2. Copy the mermaid code block"
Write-Host "3. Go to https://mermaid.live/"
Write-Host "4. Paste and download as PNG/SVG"
Write-Host "5. Save in 'diagrams_export' folder"
Write-Host ""
Write-Host "📌 Recommended filenames:"
foreach ($diagram in $diagrams) {
    Write-Host "   - $diagram.png"
}
```

**Or use this manual approach:**

### Manual Export (Step-by-Step):

#### **Diagram 1: Class Diagram**
- Location: `UML_DIAGRAMS.md` (Section 1)
- Copy mermaid code → https://mermaid.live/ → Export
- Save as: `class_diagram.png`

#### **Diagram 2: Sequence - Login & Add Employee**
- Location: `UML_DIAGRAMS.md` (Section 2)
- Export as: `sequence_login.png`

#### **Diagram 3: Sequence - Generate Payroll**
- Location: `UML_DIAGRAMS.md` (Section 3)
- Export as: `sequence_payroll.png`

#### **Diagram 4: Sequence - Apply Leave**
- Location: `UML_DIAGRAMS.md` (Section 4)
- Export as: `sequence_leave.png`

#### **Diagram 5: Use Case Diagram**
- Location: `UML_DIAGRAMS.md` (Section 5)
- Export as: `usecase_diagram.png`

#### **Diagram 6: Architecture Diagram**
- Location: `UML_DIAGRAMS.md` (Section 6)
- This is ASCII art - screenshot directly or recreate in draw.io

#### **Diagram 7: ERD (Entity Relationship)**
- Location: `UML_DIAGRAMS.md` (Section 7)
- Export as: `erd_diagram.png`

#### **Diagram 8: Activity Diagram**
- Location: `UML_DIAGRAMS.md` (Section 8)
- Export as: `activity_diagram.png`

#### **Diagram 9: State Diagram**
- Location: `UML_DIAGRAMS.md` (Section 9)
- Export as: `state_diagram.png`

#### **Diagram 10: Component Diagram**
- Location: `UML_DIAGRAMS.md` (Section 10)
- Export as: `component_diagram.png`

---

## 📊 Method 4: Automated Export (Using CLI Tool)

If you want to automate image generation, install Mermaid CLI:

```bash
# Install Mermaid CLI (requires Node.js)
npm install -g @mermaid-js/mermaid-cli

# Convert a diagram to PNG
mmdc -i diagram.mmd -o diagram.png

# Convert to SVG
mmdc -i diagram.mmd -o diagram.svg

# Batch convert all diagrams
mmdc -i UML_DIAGRAMS.md -o diagrams_export/
```

**Note:** This requires Node.js and npm to be installed.

---

## 🎓 For Your Presentation

### Recommended Approach:

1. **View in VS Code:** 
   - During development and review
   - Use `Ctrl+Shift+V` to preview

2. **Export 3-5 Key Diagrams as PNG:**
   - Class Diagram (shows architecture)
   - Sequence Diagram - Payroll (shows flow)
   - ERD (shows database)
   - Use Case Diagram (shows features)
   - Component Diagram (shows layers)

3. **Add to PowerPoint/Google Slides:**
   - Insert these PNG images
   - Use them to explain your project architecture

### Pro Tips:

✅ **High Resolution:** Export as SVG for best quality, convert to PNG if needed
✅ **Consistency:** Use same export settings for all diagrams
✅ **File Names:** Use descriptive names like `hrms_class_diagram.png`
✅ **Backup:** Keep both MD file and exported images

---

## 🔗 Quick Links

- **Mermaid Live Editor:** https://mermaid.live/
- **Mermaid Documentation:** https://mermaid.js.org/
- **VS Code Extension:** Already installed! (bierner.markdown-mermaid)

---

## ✅ What's Already Done

- [x] Mermaid extension installed in VS Code
- [x] UML_DIAGRAMS.md created with 10 diagrams
- [x] Mermaid Live Editor opened in browser
- [ ] Export diagrams as images (your turn!)

---

## 🆘 Troubleshooting

**Q: Diagrams not showing in VS Code preview?**
- Make sure the extension is enabled
- Reload VS Code window (Ctrl+Shift+P → "Reload Window")

**Q: Mermaid Live not rendering diagram?**
- Check for syntax errors
- Make sure you copied only the code between ```mermaid and ```

**Q: Need to edit diagrams?**
- Edit directly in UML_DIAGRAMS.md
- Preview updates automatically in VS Code

---

**Good luck with your presentation! 🚀**
