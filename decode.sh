#!/usr/bin/env bash
# decode.sh — sums values at odd positions (1st, 3rd, 5th, …) from a file.
# Usage: bash decode.sh <path-to-file>

set -euo pipefail

if [[ $# -lt 1 ]]; then
    echo "Usage: $0 <file>" >&2
    exit 1
fi

file="$1"

if [[ ! -f "$file" ]]; then
    echo "Error: file '$file' not found." >&2
    exit 1
fi

sum=0
index=0

while read -r line; do
    read -ra tokens <<< "$line"
    for num in "${tokens[@]}"; do
        index=$((index + 1))
        if (( index % 2 == 1 )); then
            sum=$((sum + num))
        fi
    done
done < "$file"

echo "$sum"
