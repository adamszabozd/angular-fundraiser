export function numberToString(x: number) {
    if (x != undefined) {
        return (Number)(x.toFixed(2)).toLocaleString();
    } else {
        return "";
    }
}

export function rateNumberToString(x: number) {
    return (Number)(x.toFixed(4)).toLocaleString();
}
