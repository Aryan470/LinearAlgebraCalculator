function processExpression() {
    let form = document.getElementById("inputForm");
    let expression = form.elements.namedItem("expression").value;

    const data = new FormData();
    data.append("expression", expression);

    const xhr = new XMLHttpRequest();
    const url = "/query";
    xhr.open("POST", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            document.getElementById("output").innerHTML = "Result: " + xhr.responseText;
        }
    };
    xhr.send(data);
}