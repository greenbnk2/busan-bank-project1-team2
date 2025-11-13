document.addEventListener('DOMContentLoaded', async () => {
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

    // 기준일자(오늘) 표시
    const now = new Date();
    const y = now.getFullYear(), m = String(now.getMonth() + 1).padStart(2, '0'), d = String(now.getDate()).padStart(2, '0');
    const ref = `${y}-${m}-${d}`;
    const refEl = document.getElementById('rateRefDate');
    if (refEl) refEl.textContent = ref;

    // 탭-패널 매핑
    const tabBtns = document.querySelectorAll('.tabs .tab');
    const panels = {
        guide: document.getElementById('panel-guide'),
        rate: document.getElementById('panel-rate'),
        terms: document.getElementById('panel-terms'),
    };

    // 탭 전환
    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            // 탭 active/aria
            tabBtns.forEach(b => { b.classList.remove('active'); b.setAttribute('aria-selected', 'false'); });
            btn.classList.add('active'); btn.setAttribute('aria-selected', 'true');

            // 패널 전환
            const key = btn.dataset.tab;           // guide | rate | terms
            Object.values(panels).forEach(p => p.classList.remove('active'));
            panels[key].classList.add('active');

            // 스크롤 보정(선택): 탭 전환 시 본문 상단으로
            // panels[key].scrollIntoView({block:'start', behavior:'smooth'});
        });
    });

    /* 상품 데이터 삽입 */
    async function fetchProduct() {
        try {
            const pid = document.getElementById('script')?.dataset.pid;
            if (!pid) throw new Error('pid 없음');
            const response = await fetch(`/BNK/product/details/${pid}`, {method: "GET"});
            // console.log(response);
            if (!response.ok) throw new Error('상품 정보를 가져오는 도중 문제 발생');
            const productInfo = await response.json();
            console.log(productInfo);
            return productInfo;
        } catch (e) {
            console.error(e);
        }
    }

    function renderProduct(productInfo) {
        const title = document.getElementsByClassName('h-title')[0];
        const pbirate = document.getElementById('baseRate');
        const phirate = document.getElementById('maxRate');
        const pcprd = document.getElementById('contractPeriod');
        const pjnfee = document.getElementById('joinFee');
        const prmthd = document.querySelector('div[aria-label="가입방법"]');
        const prmthdValues = (productInfo.prmthd ?? '').split(',')
            .map(s => s.trim()).filter(Boolean);

        title.innerText = productInfo.pname;
        pbirate.innerText = productInfo.pbirate + '%';
        phirate.innerText = productInfo.phirate + '%';
        pcprd.innerText = productInfo.pcprd;
        pjnfee.innerText = productInfo.pjnfee;

        if (prmthdValues.length) {
            prmthd.style.display = "flex";
            for (const prmthdValue of prmthdValues) {
                const pill = `<span class="pill">${prmthdValue}</span>`
                prmthd.insertAdjacentHTML("beforeend", pill);
            }
        } else {
            prmthd.style.display = "none";
        }
    }

    fetchProduct().then(data => {
        renderProduct(data);
        try {
            // console.log(JSON.parse(data.pterms));
            renderTerms(JSON.parse(data.pterms));
        } catch (e) {
            console.error('pterms JSON 파싱 실패');
            return null;
        }
    });

    // 약관 데이터 (원하면 URL을 실제 파일로 교체)
    // const TERMS = [
    //     { title: '예금거래기본약관', link: '#' },
    //     { title: '적립식예금약관', link: '#' },
    //     { title: '주택청약종합저축 특약', link: '#' },
    //     { title: '주택청약종합저축 상품설명서', link: '#' },
    // ];

    function renderTerms(terms) {
        const ul = document.getElementById('termsList');
        if (!ul) return;
        ul.innerHTML = terms.map(t => `
      <li class="doc-item" tabindex="0" data-link="${t.link}">
        <span class="title">${t.title}</span>
        <svg class="chev" viewBox="0 0 24 24" aria-hidden="true">
          <path fill="currentColor" d="M9 6l6 6-6 6"></path>
        </svg>
      </li>
    `).join('');

        // 클릭/엔터 시 열기
        ul.querySelectorAll('.doc-item').forEach(li => {
            const open = () => {
                const href = li.dataset.link;
                if (href && href !== '#') window.open(href, '_blank');
                // 데모일 땐 알림만
                else alert(li.querySelector('.title').textContent + ' 문서를 연결해주세요.');
            };
            li.addEventListener('click', open);
            li.addEventListener('keydown', e => { if (e.key === 'Enter') open(); });
        });
    }

    // 초기 렌더 한 번 실행
    // renderTerms();
});