/* Signup, no duplicated usernames allowed because what if two people share the same password, the first person in the
* table will be logged in, so we need the username to remain different. Login and signup ids conflict btw.*/
if (window.location.pathname.split('/').pop() === 'signup') {
    document.getElementById('username').addEventListener('change', async e => {
        let valid = await fetch(`http://localhost:8080/isUsernameAvailable?username=${e.target.value}`)
        valid = await valid.text()
        if (valid === 'false') {
            alert('Username taken')
            e.target.value = ''
        }
    })
}

/* Logout, note if <a> tag used it'll break unless you insert e.preventDefault() before it hence <button> used */
document.getElementById('logout').addEventListener('click', async (e) => {
    await fetch(`http://localhost:8080/deleteSession`)
    document.getElementById("avatar").innerText = "N/A";
    window.location = "/";
})

/* Avatar, on load */
let cookies = document.cookie.split(';');
let sessionID = "";

cookies.forEach(cookie => {
    let curr = cookie.split('=')
    if (curr[0].trim() === "sessionID") {
        sessionID = curr[1]
    }
})

document.getElementById("avatar").innerText = sessionID ? sessionID : "N/A";
