document.addEventListener('DOMContentLoaded', () => {
    /* 데모 데이터 */
    const PRODUCT = {
        name: 'BNK 스마트정기예금',
        baseRate: 2.8, maxRate: 3.5   // 연이율(%)
    };
    const fmtKR = n => n.toLocaleString('ko-KR');

    /* 초기 표시 */
    document.getElementById('baseRate').textContent = PRODUCT.baseRate.toFixed(2) + '%';
    document.getElementById('maxRate').textContent = PRODUCT.maxRate.toFixed(2) + '%';
    document.getElementById('rateBig').textContent = PRODUCT.maxRate.toFixed(2) + '%';

    /* 이자 계산(단리 데모) */
    function recalc() {
        const amount = Math.max(0, Number(document.getElementById('amount').value || 0));
        const months = Number(document.getElementById('period').value);
        const r = PRODUCT.maxRate / 100;

        const interest = Math.floor(amount * r * (months / 12));
        const total = amount + interest;

        document.getElementById('sumInt').textContent = fmtKR(interest) + '원';
        document.getElementById('sumAmt').textContent = fmtKR(total) + '원';
    }

    document.getElementById('amount').addEventListener('input', recalc);
    document.getElementById('period').addEventListener('change', recalc);
    recalc();

    /* (옵션) 탭은 데모용 상태토글 */
    document.querySelectorAll('.tab').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.tab').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
        });
    });
});