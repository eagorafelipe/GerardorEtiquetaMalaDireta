# GeradorEtiquetasMalaDireta

![Build and Release](https://github.com/eagorafelipe/GerardorEtiquetaMalaDireta/actions/workflows/build-release.yml/badge.svg)

Aplicação Desktop para geração de etiquetas Pimaco a partir de planilhas Excel ou CSV, desenvolvida com Kotlin Multiplatform e Compose Multiplatform.

## 🚀 Funcionalidades

- 📋 Suporte a múltiplos modelos de etiquetas Pimaco (6081, 6082, 6083, 6280, 6181, A4049)
- 📂 Importação de dados de planilhas Excel (.xlsx, .xls) e CSV
- 🖨️ Geração de PDF pronto para impressão
- 💻 Interface moderna e intuitiva com Material Design 3

## 📦 Instalação

### Linux

- **Debian/Ubuntu**: Baixe o arquivo `.deb` da [última release](../../releases/latest) e instale com:
  ```bash
  sudo dpkg -i GeradorEtiquetasMalaDireta_*.deb
  ```
- **Fedora/RHEL**: Baixe o arquivo `.rpm` e instale com:
  ```bash
  sudo rpm -i GeradorEtiquetasMalaDireta_*.rpm
  ```
- **AppImage**: Baixe o `.AppImage`, dê permissão de execução e rode diretamente:
  ```bash
  chmod +x GeradorEtiquetasMalaDireta-*.AppImage
  ./GeradorEtiquetasMalaDireta-*.AppImage
  ```

### Windows

- Baixe o instalador `.msi` da [última release](../../releases/latest) e execute.

## 🛠️ Desenvolvimento

### Pré-requisitos

- JDK 17 ou superior
- Gradle 8.x

### Executando localmente

```bash
./gradlew run
```

### Compilando

```bash
# Build simples
./gradlew build

# Gerar pacotes nativos (Linux)
./gradlew packageDeb packageRpm packageAppImage

# Gerar pacotes nativos (Windows)
./gradlew packageMsi
```

## 📋 Formato da Planilha

A planilha deve conter 6 colunas na seguinte ordem:

| Nome_Sindico | Nome_Condominio | End_Condominio | Bairro_Condominio | Cidade_Condominio | Cep_Condominio |
| ------------ | --------------- | -------------- | ----------------- | ----------------- | -------------- |
| João Silva   | Cond. Sol       | Rua A, 123     | Centro            | São Paulo         | 01234-567      |

## 🏷️ Modelos de Etiquetas Suportados

| Código | Descrição              | Colunas x Linhas | Dimensões (mm) |
| ------ | ---------------------- | ---------------- | -------------- |
| 6081   | Etiqueta Endereçamento | 3 × 10           | 66.7 × 25.4    |
| 6082   | Etiqueta Endereçamento | 2 × 10           | 99.1 × 25.4    |
| 6083   | Etiqueta Endereçamento | 2 × 5            | 99.1 × 50.8    |
| 6280   | Etiqueta Remetente     | 3 × 11           | 63.5 × 25.4    |
| 6181   | Etiqueta Grande        | 2 × 7            | 101.6 × 38.1   |
| A4049  | Etiqueta Universal     | 3 × 8            | 63.5 × 33.9    |

## 🔧 Tecnologias

- **Kotlin Multiplatform** - Base do projeto multiplataforma
- **Compose Multiplatform** - Framework de UI declarativa
- **Apache POI** - Leitura de arquivos Excel
- **OpenCSV** - Leitura de arquivos CSV
- **iText 7** - Geração de PDFs

## 📄 Licença

Este projeto está sob a licença MIT.

## 🤝 Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests.

---

Desenvolvido com ❤️ usando Kotlin e Compose Multiplatform
