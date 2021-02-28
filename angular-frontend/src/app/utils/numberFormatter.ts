export function numberToString(x: number) {
    return (Number)(x.toFixed(2)).toLocaleString();
}

export function rateNumberToString(x: number) {
    return (Number)(x.toFixed(4)).toLocaleString();
}
