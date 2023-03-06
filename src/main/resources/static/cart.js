// Retrieve the cart from cookies, initialize if undefined
// https://stackoverflow.com/questions/28918232/how-do-i-persist-a-es6-map-in-localstorage-or-elsewhere
function getCart() {
    var cart;
    if (localStorage.cart == undefined) {
        cart = new Map();
    } else {
        cart = new Map(JSON.parse(localStorage.cart));
    }
    return cart;
}

// Add a given quantity of a product to the cart
function addToCart(productId, trained, quantity) {
    const cart = getCart();
    const cartId = productId.toString() + (function() { // Anonymous function to tag key
        if (trained) {return "t";}
        return "u";
    })();

    if (cart.get(cartId) == undefined) {
        cart.set(cartId, 0);
    }

    const currQuantity = cart.get(cartId);
    cart.set(cartId, currQuantity + quantity);
    localStorage.cart = JSON.stringify(Array.from(cart));
}

// Set the quantity of an item in the cart given the key
function setCartItem(key, value) {
    const cart = getCart();
    cart.set(key, value);
    localStorage.cart = JSON.stringify(Array.from(cart));
}

// Count total items in the cart
function cartCount() {
    const cart = getCart();
    var total = 0;
    cart.forEach((value) => {
        total += value;
    })
    return total;
}

// Convert a JS Map into a JSON Map
function mapStringify(map) {
    res = "{"
    map.forEach((value, key) => {
        res += "\"" + key + "\":" + parseInt(value) + ", "
    })
    res = res.slice(0, -2)
    res += "}"
    return res;
}

// Remove all items from cart
function clearCart() {
    localStorage.cart.value = null;
}

// Print the entire cart to the console (testing purposes only)
function showCart() {
    const cart = getCart();
    cart.forEach((value, key) => {
        console.log(key);
        console.log(value);
        console.log("");
    })
}

// Add the elements from the make-shift "catalogue" to the shopping cart (testing purposes only)
function addCart() {
    const prod = document.getElementById('prodId');
    const train = $('#trained');
    const quant = document.getElementById('quant');
    addToCart(parseInt(prod.value), train.is(':checked'), parseInt(quant.value));
}
