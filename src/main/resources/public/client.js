function processExpression() {
    let expression = document.getElementById("expression").value;

    const data = new FormData();
    data.append("expression", expression);

    const xhr = new XMLHttpRequest();
    const url = "/query";
    xhr.open("POST", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                processResults(xhr.responseText);
            } else {
                document.getElementById("error").innerText = xhr.responseText;
                console.log(xhr.responseText);
            }
        }
    };
    xhr.send(data);
}

// This processes a 200 response
function processResults(responseText) {
    document.getElementById("error").innerText = "";
    const response = JSON.parse(responseText);
    const type = response["type"];
    let result = "NO RESULT";
    if (type === "report") {
        if (response.message) {
            result = name + ": " + response.message;
        } else {
            result = drawContent(response.content);
        }
    } else if (type === "scalar") {
        result = drawNumber(response);
    } else if (type === "vector") {
        result = drawVector(response);
    } else if (type === "matrix") {
        result = drawMatrix(response);
    } else if (type === "function") {
        result = drawFunction(response);
    }

    document.getElementById("output").innerText = result.split("(").join("{(").split(")").join(")}");
    updateHistory();
    MathJax.typeset();
}

function updateHistory() {
    const expr = document.getElementById("expression").value;
    const out = document.getElementById("output").innerHTML;
    let newLog = document.createElement("p");
    newLog.onmouseover = function greyBackground() {this.style.backgroundColor = '#F0F0F0'};
    newLog.onmouseout = function whiteBackground() {this.style.backgroundColor = '#FFFFFF'};
    newLog.onclick = function fillExpression() {
        const content = newLog.innerHTML;
        const input = content.substring(4, content.indexOf("<br>"));
        document.getElementById("expression").value = input;
    };
    newLog.innerHTML = "In: " + expr + "<br>Out: " + out;

    document.getElementById("history").prepend(newLog);
    document.getElementById("expression").value = "";
}

function drawFunction(func) {
    const funcString = func.full;
    const parenIndex = funcString.indexOf("(");
    const name = funcString.substring(0, parenIndex);
    return "$\\mathit{" + name + "}" + funcString.substring(parenIndex) + "$";

}

function drawContent(content) {
    let output = "";
    Object.keys(content).forEach(key => {
        let thisType = content[key]["type"];

        if (thisType === "function") {
            output += drawFunction(content[key]);
        } else {
            let result;
            if (thisType === "scalar") {
                result = drawNumber(content[key]);
            } else if (thisType === "vector") {
                result = drawVector(content[key]);
            } else if (thisType === "matrix") {
                result = drawMatrix(content[key]);
            }
            output += key + " = " + result
        }
        output += "\n";
    });
    return output;
}

function drawNumber(number) {
    return "$" + number.value + "$";
}

function drawVector(vector) {
    const components = vector.components;
    let out = "$\\begin{bmatrix}";
    components.forEach(item => {
        out += item + "\\\\";
    });
    out += "\\end{bmatrix}$";
    return out;
}

function drawMatrix(matrix) {
    const components = matrix.components;
    let out = "$\\begin{bmatrix}";
    components.forEach(row => {
        row.forEach(cell => {
            out += cell + "&";
        });
        out = out.slice(0, -1);
        out += "\\\\";
    });
    out += "\\end{bmatrix}$"
    return out;
}

// Process enter key -> button click
function setUp() {
    setUpEnterClick();
}

function setUpEnterClick() {
    document.getElementById("expression").addEventListener("keyup", event => {
        if (event.keyCode === 13) {
            event.preventDefault();
            document.getElementById("enter").click();
        }
    });
}