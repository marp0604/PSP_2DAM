#!/bin/bash

# $1 es el primer parámetro pasado al script

if [ -z "$1" ]; then
    echo "¡Hola! No me has pasado tu nombre."
    exit 1
else
    echo "¡Hola, $1! Este mensaje viene del script de Bash."
    echo "La fecha de hoy es: $(date +%F)"
    exit 0
fi