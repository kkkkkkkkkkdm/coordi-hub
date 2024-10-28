let currentPage = 0;
const pageSize = 10;

// 상품 목록 조회
async function fetchProducts(page = 0) {
  try {
    const response = await fetch(
        `/api/v1/admin/products?page=${page}&size=${pageSize}`);
    const result = await response.json();

    if (!result.success) {
      throw new Error(result.error.message);
    }

    const data = result.data;
    displayProducts(data.content);
    displayPagination(data);
    currentPage = page;
  } catch (error) {
    alert('상품 목록 조회 중 오류가 발생했습니다: ' + error.message);
  }
}

// 상품 목록
function displayProducts(products) {
  const html = `
      <table>
          <thead>
              <tr>
                  <th>ID</th>
                  <th>상품명</th>
                  <th>브랜드</th>
                  <th>카테고리</th>
                  <th>가격</th>
                  <th>액션</th>
              </tr>
          </thead>
          <tbody>
              ${products.map(product => `
                  <tr>
                      <td>${product.id}</td>
                      <td>${product.name}</td>
                      <td>${product.brandName}</td>
                      <td>${product.categoryName}</td>
                      <td class="price">${product.price.toLocaleString()}원</td>
                      <td>
                          <button class="btn" onclick="showUpdateProductModal(${product.id}, '${product.name}', '${product.brandName}', '${product.categoryName}', ${product.price})">수정</button>
                          <button class="btn" onclick="deleteProduct(${product.id})">삭제</button>
                      </td>
                  </tr>
              `).join('')}
          </tbody>
      </table>
  `;
  document.getElementById('productsContainer').innerHTML = html;
}
function displayPagination(data) {
  const totalPages = data.totalPages;
  const currentPage = data.number;
  const html = `
        <button onclick="fetchProducts(0)" ${currentPage === 0 ? 'disabled'
      : ''}>
            처음
        </button>
        <button onclick="fetchProducts(${currentPage - 1})" ${currentPage === 0
      ? 'disabled' : ''}>
            이전
        </button>
        ${Array.from({length: totalPages}, (_, i) => `
            <button onclick="fetchProducts(${i})" class="${i === currentPage
      ? 'active' : ''}">
                ${i + 1}
            </button>
        `).join('')}
        <button onclick="fetchProducts(${currentPage + 1})" ${currentPage
  === totalPages - 1 ? 'disabled' : ''}>
            다음
        </button>
        <button onclick="fetchProducts(${totalPages - 1})" ${currentPage
  === totalPages - 1 ? 'disabled' : ''}>
            마지막
        </button>
    `;
  document.getElementById('pagination').innerHTML = html;
}

// 브랜드, 카테고리 목록
async function fetchBrandsAndCategories() {
  try {
      const brandResponse = await fetch('/api/v1/admin/brands?size=100');
      const brandResult = await brandResponse.json();
      
      if (!brandResult.success) {
          throw new Error(brandResult.error.message);
      }

      const brandSelect = document.getElementById('brandName');
      const updateBrandSelect = document.getElementById('updateBrandName');
      const brandOptions = brandResult.data.content
          .map(brand => `<option value="${brand.name}">${brand.name}</option>`)
          .join('');
          
      brandSelect.innerHTML = brandOptions;
      updateBrandSelect.innerHTML = brandOptions;

      const categories = [
          { name: "상의" },
          { name: "하의" },
          { name: "아우터" },
          { name: "스니커즈" },
          { name: "가방" },
          { name: "모자" },
          { name: "양말" },
          { name: "액세서리" }
      ];
      
      const categorySelect = document.getElementById('categoryName');
      const updateCategorySelect = document.getElementById('updateCategoryName');

      const categoryOptions = categories
          .map(category => `<option value="${category.name}">${category.name}</option>`)
          .join('');
          
      categorySelect.innerHTML = categoryOptions;
      updateCategorySelect.innerHTML = categoryOptions;
  } catch (error) {
      alert('브랜드/카테고리 목록 조회 중 오류가 발생했습니다: ' + error.message);
  }
}

function showCreateProductModal() {
  fetchBrandsAndCategories();
  document.getElementById('createProductModal').style.display = 'block';
}

function showUpdateProductModal(id, name, brandName, categoryName, price) {
  fetchBrandsAndCategories().then(() => {
      document.getElementById('updateProductId').value = id;
      document.getElementById('updateProductName').value = name;
      document.getElementById('updateBrandName').value = brandName;
      document.getElementById('updateCategoryName').value = categoryName;
      document.getElementById('updateProductPrice').value = price;      
      document.getElementById('updateProductModal').style.display = 'block';
  });
}

function hideModal(modalId) {
  document.getElementById(modalId).style.display = 'none';
}

// 상품 등록
document.getElementById('createProductForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  const productData = {
      name: formData.get('name'),
      brandName: formData.get('brandName'),
      categoryName: formData.get('categoryName'),
      price: parseInt(formData.get('price'))
  };
  
  try {
      const response = await fetch('/api/v1/admin/products', {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json'
          },
          body: JSON.stringify(productData)
      });
      
      if (!response.ok) {
          throw new Error('등록 실패: ' + response.status);
      }

      const result = await response.json();
      if (!result.success) {
          throw new Error(result.error?.message || '등록 실패');
      }

      hideModal('createProductModal');
      e.target.reset();
      fetchProducts(currentPage);
  } catch (error) {
      alert('상품 등록 중 오류가 발생했습니다: ' + error.message);
  }
});

// 상품 수정
document.getElementById('updateProductForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  const productId = document.getElementById('updateProductId').value;
  const productData = {
      name: formData.get('name'),
      brandName: formData.get('brandName'),
      categoryName: formData.get('categoryName'),
      price: parseInt(formData.get('price'))
  };
  
  try {
      const response = await fetch(`/api/v1/admin/products/${productId}`, {
          method: 'PUT',
          headers: {
              'Content-Type': 'application/json'
          },
          body: JSON.stringify(productData)
      });

      if (!response.ok) {
          throw new Error('수정 실패: ' + response.status);
      }

      const result = await response.json();
      if (!result.success) {
          throw new Error(result.error?.message || '수정 실패');
      }

      hideModal('updateProductModal');
      fetchProducts(currentPage);
  } catch (error) {
      alert('상품 수정 중 오류가 발생했습니다: ' + error.message);
  }
});


// 상품 삭제
async function deleteProduct(productId) {
  if (!confirm('정말 삭제하시겠습니까?')) {
      return;
  }
  
  try {
      const response = await fetch(`/api/v1/admin/products/${productId}`, {
          method: 'DELETE',
          headers: {
              'Content-Type': 'application/json'
          }
      });

      if (!response.ok) {
          throw new Error('삭제 실패: ' + response.status);
      }

      const result = await response.json();
      if (!result.success) {
          throw new Error(result.error?.message || '삭제 실패');
      }

      fetchProducts(currentPage);
  } catch (error) {
      alert('상품 삭제 중 오류가 발생했습니다: ' + error.message);
  }
}

// 초기 로드
fetchProducts();