"""
Parte 1 del análisis — Comparativa de implementaciones de List.

Genera una figura por cada implementación de lista, mostrando todas las
operaciones en función del tamaño de entrada (escala log-log), tal como
se ilustra en el ejemplo del enunciado.

Uso:
    python analysis/plot_lists.py
"""

import math
import os

import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import pandas as pd

CSV_PATH   = "results/benchmark_results.csv"
OUTPUT_DIR = "results/graphs"

LIST_IMPLS = [
    "SinglyLinkedList",
    "SinglyLinkedListWithTail",
    "DoublyLinkedList",
    "DoublyLinkedListWithTail",
]

OPERATIONS = ["pushFront", "pushBack", "popFront", "popBack",
              "find", "erase", "addBefore", "addAfter"]

COLORS = {
    "pushFront": "#1f77b4",
    "pushBack":  "#ff7f0e",
    "popFront":  "#2ca02c",
    "popBack":   "#d62728",
    "find":      "#9467bd",
    "erase":     "#8c564b",
    "addBefore": "#e377c2",
    "addAfter":  "#7f7f7f",
}

NICE_NAMES = {
    "SinglyLinkedList":         "Single Linked List – No Tail",
    "SinglyLinkedListWithTail": "Single Linked List – With Tail",
    "DoublyLinkedList":         "Doubly Linked List – No Tail",
    "DoublyLinkedListWithTail": "Doubly Linked List – With Tail",
}


def main():
    if not os.path.exists(CSV_PATH):
        print(f"[ERROR] No se encontró {CSV_PATH}.")
        print("        Ejecutá primero: bash run.sh")
        return

    os.makedirs(OUTPUT_DIR, exist_ok=True)
    df = pd.read_csv(CSV_PATH)

    # Convert ns → µs for readability
    df["time_us"] = df["time_ns"] / 1_000.0

    for impl in LIST_IMPLS:
        subset = df[df["structure"] == impl]
        if subset.empty:
            print(f"[WARN] Sin datos para {impl}")
            continue

        fig, ax = plt.subplots(figsize=(10, 6))

        for op in OPERATIONS:
            op_data = subset[subset["operation"] == op].sort_values("size")
            if op_data.empty:
                continue
            ax.plot(op_data["size"], op_data["time_us"],
                    marker="o", label=op, color=COLORS.get(op))

        ax.set_xscale("log")
        ax.set_yscale("log")
        ax.set_xlabel("Tamaño de la lista (n)", fontsize=12)
        ax.set_ylabel("Promedio Microsegundos [µs] — Escala Logarítmica", fontsize=11)
        ax.set_title(NICE_NAMES.get(impl, impl), fontsize=14, fontweight="bold")
        ax.legend(loc="upper left", fontsize=9)
        ax.grid(True, which="both", linestyle="--", alpha=0.4)
        ax.xaxis.set_major_formatter(
            ticker.FuncFormatter(lambda x, _: f"10^{int(round(math.log10(x)))}" if x > 0 else "0"))

        fname = impl.replace(" ", "_") + ".png"
        fpath = os.path.join(OUTPUT_DIR, fname)
        fig.tight_layout()
        fig.savefig(fpath, dpi=150)
        plt.close(fig)
        print(f"[OK] {fpath}")

    # Bonus: one figure per operation comparing all 4 implementations
    for op in OPERATIONS:
        op_data = df[(df["structure"].isin(LIST_IMPLS)) & (df["operation"] == op)]
        if op_data.empty:
            continue

        fig, ax = plt.subplots(figsize=(10, 5))
        for impl in LIST_IMPLS:
            d = op_data[op_data["structure"] == impl].sort_values("size")
            ax.plot(d["size"], d["time_us"], marker="o",
                    label=NICE_NAMES.get(impl, impl))

        ax.set_xscale("log")
        ax.set_yscale("log")
        ax.set_xlabel("Tamaño de la lista (n)", fontsize=12)
        ax.set_ylabel("Microsegundos [µs]", fontsize=11)
        ax.set_title(f"Comparativa de implementaciones — {op}", fontsize=13, fontweight="bold")
        ax.legend(fontsize=8)
        ax.grid(True, which="both", linestyle="--", alpha=0.4)

        fname = f"compare_{op}.png"
        fpath = os.path.join(OUTPUT_DIR, fname)
        fig.tight_layout()
        fig.savefig(fpath, dpi=150)
        plt.close(fig)
        print(f"[OK] {fpath}")

    print(f"\nGráficos guardados en: {OUTPUT_DIR}/")


if __name__ == "__main__":
    main()
