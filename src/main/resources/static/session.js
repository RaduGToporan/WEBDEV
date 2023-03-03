document.getElementById("delete_session").addEventListener("click", () => {
    document.cookie = 'sessionID=null; expires=' + new Date(1999, 11, 30).toUTCString();
})