"""
Parte 2 del análisis — MyStack y MyQueue.

Genera una figura para ArrayStack y otra para ArrayQueue, mostrando
todas sus operaciones en función del tamaño de entrada (escala log-log).

Uso:
    python analysis/plot_stack_queue.py
"""

import pandas as pd
import matplotlib.pyplot as plt
import os

CSV_PATH   = "results/benchmark_results.csv"
OUTPUT_DIR = "results/graphs"

STRUCTURES = {
    "ArrayStack": {
        "title": "ArrayStack (Arreglo Dinámico Circular)",
        "ops":   ["push", "peek", "pop", "delete", "size", "isEmpty"],
    },
    "ArrayQueue": {
        "title": "ArrayQueue (Arreglo Circular Dinámico)",
        "ops":   ["enqueue", "front", "dequeue", "delete", "size", "isEmpty"],
    },
}

COLORS = ["#1f77b4", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd", "#8c564b"]


def main():
    if not os.path.exists(CSV_PATH):
        print(f"[ERROR] No se encontró {CSV_PATH}. Ejecutá primero: bash run.sh")
        return

    os.makedirs(OUTPUT_DIR, exist_ok=True)
    df = pd.read_csv(CSV_PATH)
    df["time_us"] = df["time_ns"] / 1_000.0

    for struct, meta in STRUCTURES.items():
        subset = df[df["structure"] == struct]
        if subset.empty:
            print(f"[WARN] Sin datos para {struct}")
            continue

        fig, ax = plt.subplots(figsize=(10, 6))

        for op, color in zip(meta["ops"], COLORS):
            op_data = subset[subset["operation"] == op].sort_values("size")
            if op_data.empty:
                continue
            ax.plot(op_data["size"], op_data["time_us"],
                    marker="o", label=op, color=color)

        ax.set_xscale("log")
        ax.set_yscale("log")
        ax.set_xlabel("Tamaño de entrada (n)", fontsize=12)
        ax.set_ylabel("Promedio Microsegundos [µs] — Escala Logarítmica", fontsize=11)
        ax.set_title(meta["title"], fontsize=14, fontweight="bold")
        ax.legend(loc="upper left", fontsize=9)
        ax.grid(True, which="both", linestyle="--", alpha=0.4)

        fname = f"{struct}.png"
        fpath = os.path.join(OUTPUT_DIR, fname)
        fig.tight_layout()
        fig.savefig(fpath, dpi=150)
        plt.close(fig)
        print(f"[OK] {fpath}")

    # Comparison: Stack vs Queue for operations that both share
    # (push/enqueue and pop/dequeue are functionally equivalent)
    fig, axes = plt.subplots(1, 2, figsize=(14, 5))

    pairs = [
        ("push",    "ArrayStack", "enqueue", "ArrayQueue", axes[0], "Inserción (push / enqueue)"),
        ("pop",     "ArrayStack", "dequeue", "ArrayQueue", axes[1], "Extracción (pop / dequeue)"),
    ]
    for op1, s1, op2, s2, ax, title in pairs:
        d1 = df[(df["structure"] == s1) & (df["operation"] == op1)].sort_values("size")
        d2 = df[(df["structure"] == s2) & (df["operation"] == op2)].sort_values("size")
        if not d1.empty:
            ax.plot(d1["size"], d1["time_us"], marker="o", label=f"{s1}.{op1}")
        if not d2.empty:
            ax.plot(d2["size"], d2["time_us"], marker="s", label=f"{s2}.{op2}")
        ax.set_xscale("log")
        ax.set_yscale("log")
        ax.set_xlabel("Tamaño (n)", fontsize=11)
        ax.set_ylabel("µs", fontsize=11)
        ax.set_title(title, fontsize=12, fontweight="bold")
        ax.legend(fontsize=9)
        ax.grid(True, which="both", linestyle="--", alpha=0.4)

    fpath = os.path.join(OUTPUT_DIR, "stack_vs_queue.png")
    fig.suptitle("Stack vs Queue — Operaciones equivalentes", fontsize=14, fontweight="bold")
    fig.tight_layout()
    fig.savefig(fpath, dpi=150)
    plt.close(fig)
    print(f"[OK] {fpath}")

    print(f"\nGráficos guardados en: {OUTPUT_DIR}/")


if __name__ == "__main__":
    main()
