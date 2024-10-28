async function fetchCategoryLowest() {
    try {
        const response = await fetch('/api/v1/coordi/categories/lowest-prices');
        const result = await response.json();
        
        if (!result.success) {
            throw new Error(result.error.message);
        }

        const data = result.data;
        const html = `
            <table>
                <tr>
                    <th>카테고리</th>
                    <th>브랜드</th>
                    <th>가격</th>
                </tr>
                ${data.categories.map(item => `
                    <tr>
                        <td>${item.category}</td>
                        <td>${item.brand}</td>
                        <td class="price">${item.price.toLocaleString()}원</td>
                    </tr>
                `).join('')}
                <tr class="total-row">
                    <td colspan="2">총 가격</td>
                    <td class="price">${data.totalPrice.toLocaleString()}원</td>
                </tr>
            </table>
        `;
        
        document.getElementById('resultContainer').innerHTML = html;
    } catch (error) {
        alert('데이터 조회 중 오류가 발생했습니다: ' + error.message);
    }
}