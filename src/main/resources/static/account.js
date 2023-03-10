/* login
* Check on page load if cookie called 'valid_credentials' is 'true' or 'false'. If user keeps submits form with wrong
* input and refreshes, the form is resubmitted and causes the alert box to appear, hence redirect in post mapping. */
if (window.location.pathname.split('/').pop() === 'login') {
    let key = 'valid_credentials'
    let sessionID = getCookie(key, 'true');

    if (sessionID === 'false') {
        alert('Invalid username or password')
        document.cookie = key + "=;expires=" + new Date(Date.UTC(0, 0, 0)).toUTCString() + ';'
    }
}

/* signup
* No duplicated usernames allowed because what if two people share the same password, the first person in the
* database will be logged in, so we need the username to remain different. Login and signup both use id "username",
* hence the if block. */
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

/* logout
* Note if <a> tag used, the redirect will cause the fetch request to be cancelled */
document.getElementById('logout')?.addEventListener('click', async (e) => {
    await fetch(`http://localhost:8080/deleteSession`)
    window.location = "/"
})

/* cart
* user shouldn't be able to pay for cart if not logged in */
document.getElementById('pay').addEventListener('submit', e => {
    if (getCookie("sessionID", "absent") === "absent") {
        e.preventDefault()
        alert("You must be logged in")
        window.location = "/login"
    }
})

/* Utility */
function getCookie(key, def) {
    let res = def

    document.cookie.split(';').forEach(cookie => {
        let pair = cookie.split('=')
        if (pair[0].trim() === key) {
            res = pair[1].trim()
        }
    })

    return res
}