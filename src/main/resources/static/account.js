document.getElementById("delete_session").addEventListener("click", () => {
    document.cookie = 'sessionID=null; expires=' + new Date(1999, 11, 30).toUTCString();
})

document.getElementById("username")?.addEventListener("change", async e => {
    let valid = await fetch(`http://localhost:8080/validateUsername?username=${e.target.value}`);
    valid = await valid.text()
    if (valid === "false") {
        alert("Username taken")
        e.target.value = ""
    }
})