#!/usr/bin/env bash
# Compila y ejecuta el benchmark desde la raíz del proyecto.
# Uso: bash run.sh

set -e

# pwd -W da rutas estilo Windows (C:/...) que javac entiende en Git Bash
ROOT="$(cd "$(dirname "$0")" && pwd -W)"
OUT="$ROOT/out/classes"
SOURCES="$ROOT/out/sources.txt"

echo "==> Compilando..."
mkdir -p "$OUT"

# Genera lista de fuentes (solo carpeta app/) con rutas Windows entre comillas
find "$ROOT/app" -name "*.java" | while IFS= read -r f; do
    cygpath -m "$f"
done | awk '{printf "\"%s\"\n", $0}' > "$SOURCES"

javac -encoding UTF-8 -d "$OUT" @"$SOURCES"

echo "==> Ejecutando..."
java -cp "$OUT" app.benchmark.Main
