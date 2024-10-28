let currentPage = 0;
const pageSize = 5;

async function fetchBrands(page = 0) {
    try {
        const response = await fetch(`/api/v1/admin/brands?page=${page}&size=${pageSize}`);
        const result = await response.json();
        
        if (!result.success) {
            throw new Error(result.error.message);
        }

        const data = result.data;
        displayBrands(data.content);
        displayPagination(data);
        currentPage = page;
    } catch (error) {
        alert('브랜드 목록 조회 중 오류가 발생했습니다: ' + error.message);
    }
}

function displayBrands(brands) {
    const html = `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>브랜드명</th>
                    <th>상태</th>
                    <th>액션</th>
                </tr>
            </thead>
            <tbody>
                ${brands.map(brand => `
                    <tr>
                        <td>${brand.id}</td>
                        <td>${brand.name}</td>
                        <td>${brand.enabled ? '활성' : '비활성'}</td>
                        <td>
                            <button onclick="showUpdateBrandModal(${JSON.stringify(brand).replace(/"/g, '&quot;')})" class="btn">수정</button>
                            <button onclick="deleteBrand(${brand.id})" class="btn">삭제</button>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
    document.getElementById('brandsContainer').innerHTML = html;
}

function displayPagination(data) {
    const totalPages = data.totalPages;
    const currentPage = data.number;
    
    const html = `
        <button onclick="fetchBrands(0)" ${currentPage === 0 ? 'disabled' : ''}>
            처음
        </button>
        <button onclick="fetchBrands(${currentPage - 1})" ${currentPage === 0 ? 'disabled' : ''}>
            이전
        </button>
        ${Array.from({length: totalPages}, (_, i) => `
            <button onclick="fetchBrands(${i})" class="${i === currentPage ? 'active' : ''}">
                ${i + 1}
            </button>
        `).join('')}
        <button onclick="fetchBrands(${currentPage + 1})" ${currentPage === totalPages - 1 ? 'disabled' : ''}>
            다음
        </button>
        <button onclick="fetchBrands(${totalPages - 1})" ${currentPage === totalPages - 1 ? 'disabled' : ''}>
            마지막
        </button>
    `;
    document.getElementById('pagination').innerHTML = html;
}

// 모달 관련 함수들
function showCreateBrandModal() {
    document.getElementById('createBrandModal').style.display = 'block';
}

function showUpdateBrandModal(brand) {
    document.getElementById('updateBrandId').value = brand.id;
    document.getElementById('updateBrandName').value = brand.name;
    document.getElementById('updateBrandEnabled').value = brand.enabled;
    document.getElementById('updateBrandModal').style.display = 'block';
}

function hideModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// 브랜드 등록
document.getElementById('createBrandForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const brandData = {
        name: formData.get('name'),
        enabled: formData.get('enabled') === 'true'
    };
    
    try {
        const response = await fetch('/api/v1/admin/brands', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(brandData)
        });
        const result = await response.json();
        
        if (!result.success) {
            throw new Error(result.error.message);
        }

        hideModal('createBrandModal');
        e.target.reset();
        fetchBrands(currentPage);
    } catch (error) {
        alert('브랜드 등록 중 오류가 발생했습니다: ' + error.message);
    }
});

// 브랜드 수정
document.getElementById('updateBrandForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('updateBrandId').value;
    const formData = new FormData(e.target);
    const brandData = {
        name: formData.get('name'),
        enabled: formData.get('enabled') === 'true'
    };
    
    try {
        const response = await fetch(`/api/v1/admin/brands/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(brandData)
        });
        const result = await response.json();
        
        if (!result.success) {
            throw new Error(result.error.message);
        }

        hideModal('updateBrandModal');
        fetchBrands(currentPage);
    } catch (error) {
        alert('브랜드 수정 중 오류가 발생했습니다: ' + error.message);
    }
});

// 브랜드 삭제
async function deleteBrand(id) {
    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }
    
    try {
        const response = await fetch(`/api/v1/admin/brands/${id}`, {
            method: 'DELETE'
        });
        const result = await response.json();
        
        if (!result.success) {
            throw new Error(result.error.message);
        }

        fetchBrands(currentPage);
    } catch (error) {
        alert('브랜드 삭제 중 오류가 발생했습니다: ' + error.message);
    }
}

// 초기 로드
fetchBrands();