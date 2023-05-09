let products = new Map();
let cart = [];
let orderHistory = [];

const authState = {
  authToken: localStorage.getItem('authToken'),
  userId: localStorage.getItem('userId'),
  username: localStorage.getItem('username'),
};

const authStateNode = document.querySelector('div#auth-state')

if (authState.authToken) {
  checkAuthToken()
} else {
  initAuth()
}

document.addEventListener("DOMContentLoaded", async () => {
  await loadProducts();
  await loadCart();
  await loadOrderHistory();

  const orderHistoryButton = document.querySelector('button#order-history-btn')
  orderHistoryButton.onclick = function() {
    const orderHistoryModal = document.querySelector("div#order-history-modal");
    orderHistoryModal.style.display = "block";

    orderHistoryModal.querySelector('.modal-close-span').onclick = function() {
      orderHistoryModal.style.display = "none";
    }
    window.onclick = event => event.target === orderHistoryModal && (orderHistoryModal.style.display = "none")

    renderOrderHistory()
  }
});

async function checkAuthToken() {
  const response = await fetch(`http://localhost:8080/auth/check_token_validity?${new URLSearchParams({
    auth_token: authState.authToken,
  })}`, {
    method: 'POST',
  })
  const {ok} = await response.json()

  if (ok) {
    authStateNode.innerHTML = `<h3>${authState.username} <button id="order-history-btn">История заказов</button></h3>`
  } else {
    invalidateAuthInLocalStorage()
    window.location.reload()
  }
}

async function initAuth() {
  authStateNode.innerHTML = `<button id="loginButton" type="button">Вход в аккаунт</button><button id="signupButton" type="button">Регистрация</button>`

  // Получить всплывающее окно
  const loginModal = document.querySelector("#loginModal");
  const signupModal = document.querySelector("#signupModal");

  // Получить кнопку для открытия всплывающего окна
  const loginButton = document.querySelector("#loginButton");
  const signupButton = document.querySelector("#signupButton");

  // Кнопки отправки данных
  const loginSubmit = document.querySelector("#login-submit")
  const signupSubmit = document.querySelector("#signup-submit")

  // Поля ввода данных
  const inputs = {
    login: {
      username: document.querySelector("input#login-username"),
      password: document.querySelector("input#login-password"),
    },
    signup: {
      username: document.querySelector("input#signup-username"),
      password: document.querySelector("input#signup-password"),
      passwordAgain: document.querySelector("input#signup-password-again"),
      tel: document.querySelector("input#phone-number"),
    },
  }

  // Открыть окно при нажатии на кнопку
  loginButton.onclick = function() {
    loginModal.style.display = "block";
  }
  signupButton.onclick = function() {
    signupModal.style.display = "block";
  }

  // Закрыть окно при нажатии на элемент закрытия
  Array.from(document.getElementsByClassName("modal-close-span")).forEach(span => span.onclick = function() {
    loginModal.style.display = "none";
    signupModal.style.display = "none";
  })

  // Закрыть окно при нажатии на любое место за пределами окна
  window.onclick = function(event) {
    if (event.target == loginModal || event.target == signupModal) {
      loginModal.style.display = "none";
      signupModal.style.display = "none";
    }
  }

  loginSubmit.onclick = async function() {
    if (inputs.login.username.value.trim().length === 0) {
      alert("Поле с именем пользователя не может быть пустым.")
      return
    }
    if (inputs.login.password.value.trim().length === 0) {
      alert("Поле с паролем не может быть пустым.")
      return
    }

    const response = await fetch(`http://localhost:8080/auth/token?${new URLSearchParams({
      username: inputs.login.username.value.trim(),
      password: inputs.login.password.value.trim(),
    })}`)
    const tokenData = await response.json()

    if (tokenData.error) {
      alert(tokenData.message)
      return
    }

    writeAuthToLocalStorage(tokenData)
    window.location.reload()
    loginModal.style.display = "none";
  }
  signupSubmit.onclick = async function() {
    if (inputs.signup.username.value.trim().length == 0) {
      alert("Некорректное имя пользователя")
      return
    }
    if (inputs.signup.password.value.trim().length < 6) {
      alert("Пароль должен быть не менее длины 6.")
      return
    }
    if (inputs.signup.passwordAgain.value.trim() !== inputs.signup.password.value.trim()) {
      alert("Пароли должны совпадать.")
      return
    }
    if (inputs.signup.tel.value.trim().length === 0) {
      alert("Контактный телефон должен быть указан для уточнения деталей.")
      return
    }

    const response = await fetch(`http://localhost:8080/auth/create_account?${new URLSearchParams({
      username: inputs.signup.username.value.trim(),
      password: inputs.signup.password.value.trim(),
      tel: inputs.signup.tel.value.trim(),
    })}`, {
      method: 'POST',
    })
    const data = await response.json()

    if (data.error) {
      alert(data.message)
      return
    }

    
    const getTokenResponse = await fetch(`http://localhost:8080/auth/token?${new URLSearchParams({
      username: inputs.signup.username.value.trim(),
      password: inputs.signup.password.value.trim(),
    })}`)
    const tokenData = await getTokenResponse.json()
    
    writeAuthToLocalStorage(tokenData)
    window.location.reload()
  }
}

async function writeAuthToLocalStorage({authToken, userId, username}) {
  window.localStorage.setItem('authToken', authToken)
  window.localStorage.setItem('userId', userId)
  window.localStorage.setItem('username', username)
}

async function invalidateAuthInLocalStorage() {
  window.localStorage.removeItem('authToken')
  window.localStorage.removeItem('userId')
  window.localStorage.removeItem('username')
}

async function loadProducts() {
  const response = await fetch("http://localhost:8080/api/products");
  const data = await response.json();
  products = new Map(data.map(({id, ...props}) => [id, props]));
  renderProducts();
}

async function loadCart() {
  const response = await fetch(`http://localhost:8080/api/cart?${new URLSearchParams({
    auth_token: authState.authToken,
  })}`);
  const data = await response.json();
  cart = data;
  renderCart();
}

async function addToCart(id) {
  const response = await fetch(`http://localhost:8080/api/cart/${id}?${new URLSearchParams({
    auth_token: authState.authToken,
  })}`, {method: "POST"});
  const data = await response.json();
  cart = data;
  renderCart();
}

async function removeFromCart(id) {
  const response = await fetch(`http://localhost:8080/api/cart/${id}?${new URLSearchParams({
    auth_token: authState.authToken,
  })}`, {
    method: "DELETE",
  });
  const data = await response.json();
  cart = data;
  renderCart();
}
async function placeOrder() {
  const response = await fetch(`http://localhost:8080/api/orders?${new URLSearchParams({
    auth_token: authState.authToken,
  })}`, {
    method: "POST",
  });
  const data = await response.json();
  
  if (data.error) {
    alert(data.message)
  } else {
    alert("Заказ оформлен!")
    await loadCart();
    renderCart();
    await loadOrderHistory();
  }
}

async function loadOrderHistory() {
  const response = await fetch(`http://localhost:8080/api/orders?${new URLSearchParams({
    auth_token: authState.authToken,
  })}`, {
    method: "GET",
  });
  orderHistory = await response.json();
}

function renderProducts() {
  const productsList = document.querySelector(".products-list");
  productsList.innerHTML = "";
  products.forEach((product, productId) => {
    const productCard = `
      <div class="product-card">
        <div class="product-image">
          <img src="${product.image}" alt="${product.name}">
        </div>
        <div class="product-info">
          <h3 class="product-name">${product.name}</h3>
          <p class="product-price">${product.price} руб./кг</p>
          <p class="product-description">${product.description}</p>
          <button class="add-to-cart" data-product-id="${productId}">Добавить в корзину</button>
        </div>
      </div>
    `;
    productsList.innerHTML += productCard;
  });
}

function renderOrderHistory() {
  const orderHistoryModal = document.querySelector('.order-history-modal-content');
  orderHistoryModal.innerHTML = '';

  orderHistory.forEach((order, i) => {
    let orderPrice = 0
    
    const orderItems = order.products.map(({productId, quantity}) => {
      const product = products.get(productId);

      if (!product) {
        return '';
      }

      orderPrice += quantity * product.price

      return `
        <div class="order-item">
          <div class="order-item-info">
            <h4 class="order-item-name">${product.name}</h4>
            <p class="order-item-price">${product.price} руб./кг</p>
            <p class="order-item-quantity">Количество: ${quantity} кг</p>
            <p class="order-item-total-price">Цена: ${quantity * product.price} руб.</p>
          </div>
        </div>
      `;
    }).join('');

    const timestamp = new Date(order.timestamp);
    const orderDate = `${timestamp.getDate() < 10 ? '0' : ''}${timestamp.getDate()}.${timestamp.getMonth() < 9 ? '0' : ''}${timestamp.getMonth() + 1}.${timestamp.getFullYear()} ${timestamp.getHours() < 10 ? '0' : ''}${timestamp.getHours()}:${timestamp.getMinutes() < 10 ? '0' : ''}${timestamp.getMinutes()}:${timestamp.getSeconds() < 10 ? '0' : ''}${timestamp.getSeconds()}`;

    const orderCard = `
      <div class="order">
        <div class="order-header">
          <h3 class="order-id">Заказ #${orderHistory.length - i} от ${orderDate}</h3>
        </div>
        <div class="order-items">
          ${orderItems}
        </div>
        <p class="order-total-price">Итого: ${orderPrice} руб.</p>
      </div>
    `;

    orderHistoryModal.innerHTML += orderCard;
  });
}

function renderCart() {
  const cartItems = document.querySelector(".cart-items");
  cartItems.innerHTML = "";
  Object.entries(cart.items).forEach(([productId, quantity]) => {
    const product = products.get(productId)

    if (!product) {
      return
    }

    const cartItemCard = `
      <div class="cart-item">
        <div class="cart-item-info">
          <h4 class="cart-item-name">${product.name}</h4>
          <p class="cart-item-price">${product.price} руб./кг</p>
          <p class="cart-item-quantity">Количество: ${quantity}</p>
          <p class="cart-item-total-price">Цена: ${quantity * product.price} руб.</p>
        </div>
        <div class="cart-item-actions">
          <button class="remove-from-cart" data-product-id="${productId}">Удалить</button>
        </div>
      </div>
    `;
    cartItems.innerHTML += cartItemCard;
  })

  const cartTotalPrice = document.querySelector(".cart-total-price");
  const totalPrice = Object.entries(cart.items).reduce((accumulator, [productId, quantity]) => {
    return accumulator + quantity * products.get(productId).price;
  }, 0);
  cartTotalPrice.innerText = `Итого: ${totalPrice} руб.`;
}

document.addEventListener("click", (event) => {
  if (event.target.classList.contains("add-to-cart")) {
    const productId = event.target.getAttribute("data-product-id");
    addToCart(productId);
  }

  if (event.target.classList.contains("remove-from-cart")) {
    const productId = event.target.getAttribute("data-product-id");
    removeFromCart(productId);
  }

  if (event.target.classList.contains("checkout-button")) {
    placeOrder();
  }
});
