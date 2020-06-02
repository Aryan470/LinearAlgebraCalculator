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
            processResults(xhr.responseText);
        }
    };
    xhr.send(data);
}

function processResults(responseText) {
    let response = JSON.parse(responseText);
    const type = response["@type"];
    let result = "NO RESULT";
    if (type == "Report") {
        if (response.message != null) {
            result = name + "<br>" + response.message;
        } else {
            result = name + "<br>" + response.content;
        }
    } else if (type == "Scalar") {
        result = response.value;

    } else if (type == "Vector") {
        result = response.components;
        
    } else if (type == "Matrix") {
        result = response.components;
    }
    document.getElementById("output").innerHTML = result;
}