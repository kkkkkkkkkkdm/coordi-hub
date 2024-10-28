async function searchCategory() {
    const categoryName = document.getElementById('categoryInput').value;
    if (!categoryName) {
        alert('카테고리명을 입력해주세요.');
        return;
    }

    try {
        const response = await fetch(`/api/v1/coordi/categories/prices/min-max?categoryName=${encodeURIComponent(categoryName)}`);
        const result = await response.json();
        
        if (!result.success) {
            throw new Error(result.error.message);
        }

        const data = result.data;
        const html = `
            <table>
                <tr>
                    <th></th>
                    <th>브랜드</th>
                    <th>가격</th>
                </tr>
                ${data.lowestPrice.map(item => `
                    <tr>
                        <td>최저가</td>
                        <td>${item.brand}</td>
                        <td class="price">${item.price.toLocaleString()}원</td>
                    </tr>
                `).join('')}
                ${data.highestPrice.map(item => `
                    <tr>
                        <td>최고가</td>
                        <td>${item.brand}</td>
                        <td class="price">${item.price.toLocaleString()}원</td>
                    </tr>
                `).join('')}
            </table>
        `;
        
        document.getElementById('resultContainer').innerHTML = html;
    } catch (error) {
        alert('데이터 조회 중 오류가 발생했습니다: ' + error.message);
    }
}

document.getElementById('categoryInput').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        searchCategory();
    }
});