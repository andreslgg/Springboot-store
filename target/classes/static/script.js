// script.js

let lastScrollTop = 0;
const navbar = document.getElementById('navbar');

window.addEventListener('scroll', () => {
    let currentScroll = window.pageYOffset || document.documentElement.scrollTop;

    if (currentScroll > lastScrollTop) {
        // Scrolling down
        navbar.style.top = '-60px'; // Adjust this value based on navbar height
    } else {
        // Scrolling up
        navbar.style.top = '0';
    }
    lastScrollTop = currentScroll <= 0 ? 0 : currentScroll; // For Mobile or negative scrolling
});


// Obtén referencias a los elementos relevantes
const productCards = document.querySelectorAll('.product-card');
const detailsContent = document.getElementById('details-content');
const productDetails = document.querySelector('.product-details');
const filterSection = document.querySelector('.filter-section');


// Agrega un event listener a cada tarjeta de producto
productCards.forEach(card => {
    card.addEventListener('mouseenter', () => {
        const productId = card.dataset.id;

        // Realiza la petición al servidor para obtener los detalles del producto
        fetch(`/api/products/${productId}`)
            .then(response => response.json())
            .then(data => {
                filterSection.classList.add('hidden');
                productDetails.classList.remove('hidden'); // Mostrar detalles
                detailsContent.innerHTML = `
                    <p><strong>Title:</strong> ${data.title}</p>
                    <p><strong>Price:</strong> $${data.price}</p>
                    <p><strong>Description:</strong> ${data.description}</p>
            <p><strong>Calification:</strong> ${data.rating.rate} (based on ${data.rating.count} ratings)</p>
            <div class="distance">
            
            </div>
                `;
            });
    });

    // Ocultar los detalles cuando el ratón sale del producto
    card.addEventListener('mouseleave', () => {
        productDetails.classList.add('hidden'); // Ocultar detalles
        filterSection.classList.remove('hidden');
    });
});
document.addEventListener('DOMContentLoaded', function () {
    // Añade el evento click a los elementos .color-box
    document.querySelectorAll('.box').forEach(box => {
        box.addEventListener('click', function () {
            // Encuentra el producto más cercano a este .color-box
            const productCard = this.closest('.product-card');
            // Obtén la URL de la imagen del atributo data-image-url
            const newImageUrl = this.getAttribute('data-image-url');
            // Cambia la imagen del producto
            if (productCard) {
                const productImage = productCard.querySelector('.product-image');
                if (productImage) {
                    productImage.src = newImageUrl;
                }
            }
        });
    });
});
document.querySelectorAll('.product-form').forEach(form => {
    form.addEventListener('submit', function(event) {
        event.preventDefault(); // Evita el envío del formulario tradicional

        const formData = new FormData(this);

        fetch('/cart/add', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    return response.text(); // Obtener el fragmento HTML como texto
                } else {
                    throw new Error('La respuesta de la red no fue exitosa.');
                }
            })
            .then(html => {
                // Actualiza el contenido del fragmento con el HTML devuelto
                const cartSidebar = document.getElementById('cart');
                cartSidebar.innerHTML = html;

                // Mostrar notificación de éxito
                const notification = document.getElementById('notification');
                notification.textContent = 'Producto agregado al carrito';
                notification.style.display = 'block';

                // Ocultar la notificación después de 3 segundos
                setTimeout(() => {
                    notification.style.display = 'none';
                }, 3000);
            })
            .catch(error => console.error('Error:', error));
    });
});

function removeFromCart(button) {
    // Obtener los parámetros
    var productId = button.getAttribute('data-product-id');
    var color = button.getAttribute('data-selected-color');


    // Convertir los parámetros a formato URL-encoded
    var params = new URLSearchParams();
    params.append('productId', productId);
    params.append('color', color || ''); // Si color es null o undefined, se envía como cadena vacía

    fetch('/cart/remove', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded' // Encabezado correcto para URL-encoded
        },
        body: params.toString() // Convertir a formato URL-encoded
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la solicitud');
            }
            return response.text(); // Obtener el HTML del fragmento actualizado
        })
        .then(html => {
            // Reemplazar el contenido del carrito con el fragmento actualizado
            const cartSidebar = document.getElementById('cart');
            cartSidebar.innerHTML = html;
        })
        .catch(error => {
            console.error('Error al eliminar el producto del carrito:', error);
        });
}



document.addEventListener('DOMContentLoaded', function() {
    const cartButton = document.getElementById('cart-button');
    const cartWindow = document.getElementById('cart-sidebar');
    const closeCart = document.getElementById('close-cart');

    // Función para abrir el carrito
    cartButton.addEventListener('click', function() {
        cartWindow.classList.remove('hidden');
    });

    // Función para cerrar el carrito
    closeCart.addEventListener('click', function() {
        cartWindow.classList.add('hidden');
    });

    // Función para cerrar el carrito cuando se hace clic fuera del sidebar
    document.addEventListener('click', function(event) {
        if (!cartWindow.contains(event.target) && !cartButton.contains(event.target)) {
            cartWindow.classList.add('hidden');
        }
    });
});


document.addEventListener('DOMContentLoaded', function() {
    const colorInputs = document.querySelectorAll('input[name="color"]');
    const colorImage = document.getElementById('color-image');
    colorInputs.forEach(input => {
        input.addEventListener('change', function() {
            const imageUrl = this.getAttribute('data-image-url');
            if (imageUrl) {
                colorImage.src = imageUrl;
                colorImage.style.display = 'block'; // Mostrar la imagen
            } else {
                colorImage.src = '';
                colorImage.style.display = 'none'; // Ocultar la imagen si no hay URL
            }
        });
    });
    const defaultInput = document.querySelector('input[name="color"][value="default"]');
    if (defaultInput) {
        defaultInput.addEventListener('change', function() {
            colorImage.src = ''; // Ocultar la imagen cuando se selecciona el color predeterminado
            colorImage.style.display = 'none';
        });
    }
});