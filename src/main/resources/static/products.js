// Retrieve the cart from cookies, initialize if undefined
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
    const cartId = productId.toString() + (function() {
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

// Count total items in the cart
function cartCount() {
    const cart = getCart();
    var total = 0;
    cart.forEach((value) => {
        total += value;
    })
    return total;
}

// Print the entire cart to the console
function showCart() {
    const cart = getCart();
    cart.forEach((value, key) => {
        console.log(key);
        console.log(value);
        console.log("");
    })
}