let products = [];
let cart = [];

const authState = {
  authToken: localStorage.getItem('authToken'),
};

const authStateHtmlNode = document.querySelector('.auth-state');
if (authState.authToken) {
  authStateHtmlNode.innerHTML = 'danila'
} else {
  authStateHtmlNode.innerHTML = `<button class="auth-btn">Вход в аккаунт</button>`
}

async function loadProducts() {
  const response = await fetch("http://localhost:8080/api/products");
  const data = await response.json();
  products = data;
  renderProducts();
}

async function loadCart() {
  const response = await fetch("http://localhost:8080/api/cart");
  const data = await response.json();
  cart = data;
  renderCart();
}

async function addToCart(id, quantity) {
  const response = await fetch(`http://localhost:8080/api/cart/${id}`, {
    method: "POST",
    mode: "cors",
    headers: {
      "Content-Type": "application/json",
    },
  });
  const data = await response.json();
  cart = data;
  renderCart();
}

async function removeFromCart(id) {
  const response = await fetch(`http://localhost:8080/api/cart/${id}`, {
    method: "DELETE",
    mode: "cors",
    headers: {
      "Content-Type": "application/json",
    },
  });
  const data = await response.json();
  cart = data;
  renderCart();
}

function renderProducts() {
  const productsList = document.querySelector(".products-list");
  productsList.innerHTML = "";
  products.forEach((product) => {
    const productCard = `
      <div class="product-card">
        <div class="product-image">
          <img src="${product.image}" alt="${product.name}">
        </div>
        <div class="product-info">
          <h3 class="product-name">${product.name}</h3>
          <p class="product-price">${product.price} руб./кг</p>
          <p class="product-description">${product.description}</p>
          <button class="add-to-cart" data-product-id="${product.id}">Добавить в корзину</button>
        </div>
      </div>
    `;
    productsList.innerHTML += productCard;
  });
}

function renderCart() {
  const cartItems = document.querySelector(".cart-items");
  cartItems.innerHTML = "";
  cart.forEach((cartItem) => {
    const cartItemPrice = cartItem.quantity * cartItem.product.price;
    const cartItemCard = `
      <div class="cart-item">
        <div class="cart-item-info">
          <h4 class="cart-item-name">${cartItem.product.name}</h4>
          <p class="cart-item-price">${cartItem.product.price} руб./кг</p>
          <p class="cart-item-quantity">Количество: ${cartItem.quantity}</p>
          <p class="cart-item-total-price">Цена: ${cartItemPrice} руб.</p>
        </div>
        <div class="cart-item-actions">
          <button class="remove-from-cart" data-product-id="${cartItem.product.id}">Удалить</button>
        </div>
      </div>
    `;
    cartItems.innerHTML += cartItemCard;
  });

  const cartTotalPrice = document.querySelector(".cart-total-price");
  const totalPrice = cart.reduce((accumulator, cartItem) => {
    return accumulator + cartItem.quantity * cartItem.product.price;
  }, 0);
  cartTotalPrice.innerText = `Итого: ${totalPrice} руб.`;
}

document.addEventListener("DOMContentLoaded", () => {
  loadProducts();
  loadCart();
});

document.addEventListener("click", (event) => {
  if (event.target.classList.contains("add-to-cart")) {
    const productId = event.target.getAttribute("data-product-id");
    addToCart(productId, 1);
  }

  if (event.target.classList.contains("remove-from-cart")) {
    const productId = event.target.getAttribute("data-product-id");
    removeFromCart(productId);
  }
});
