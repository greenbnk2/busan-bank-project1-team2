/*
    날짜 : 2025.11.20.
    이름 : 강민철
    내용 : product_insert_info.html JS 작성
 */
document.addEventListener('DOMContentLoaded', function () {
    /*======== 스탭퍼 스크립트 ========*/
    let currentStep = 1;                 // 1~5
    const totalSteps = 5;
    const state = {};                    // 모든 단계의 입력값을 여기에 저장(필요 시)

    /* 유효성 검사 정규표현식 */
    const reName = /^[가-힣]{2,10}$/;
    const reHp = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/;

    const pages = [...document.querySelectorAll('.step-page')];
    const steps = [...document.querySelectorAll('#wizardSteps .step')];
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    /* 유효성 검사 훅: 단계별로 통과하면 true 반환 */
    const validators = {
        1() {
            // 필수 입력 검증
            const form1 = document.getElementById('customerForm');
            const name = form1.name;
            const brthdt = form1.brthdt;
            const natcd = form1.natcd;
            const taxyr = form1.taxyr;
            const enlnm = form1.enlnm;
            const enfnm = form1.enfnm;
            const phone = form1.phone;
            const zipcd = form1.zipcd;
            const addr1 = form1.addr1;
            let checkValid = [false, null];
            const checkNGo = (input, comment1, id, regEx = null, comment2 = null) => {
                const span = document.getElementById(id);
                if (Array.isArray(input)) {
                    for (const inputElement of input) {
                        if (inputElement.value.trim() === "") {
                            span.innerText = comment1;
                            checkValid[0] = false;
                            if (checkValid[1] === null)
                                checkValid[1] = inputElement;
                            break;
                        } else
                            checkValid[0] = true;
                    }
                } else {
                    if (input.value.trim() === "") {
                        input.focus();
                        span.innerText = comment1;
                        checkValid[0] = false;
                        if (checkValid[1] === null)
                            checkValid[1] = input;
                    } else if (regEx !== null) {
                        if (!input.value.match(regEx)) {
                            span.innerText = comment2;
                            checkValid[0] = false;
                            if (checkValid[1] === null)
                                checkValid[1] = input;
                        }
                    } else
                        checkValid[0] = true;
                }
            }

            checkNGo(name, '성명을 입력해주세요.', 'name-comment', reName, '이름이 유효하지 않습니다.');
            checkNGo(brthdt, '생년월일을 입력해주세요.', 'brthdt-comment');
            checkNGo(natcd, '국적을 선택해주세요.', 'natcd-comment');
            checkNGo(taxyr, '귀속년도를 선택해주세요.', 'taxyr-comment');
            checkNGo([enlnm, enfnm], '영문이름을 입력해주세요.', 'ennm-comment');
            checkNGo(phone, '전화번호를 입력해주세요.', 'phone-comment', reHp, '전화번호가 유효하지 않습니다.');
            checkNGo([zipcd, addr1], '우편번호와 주소를 입력해주세요.', 'addr-comment');
            if (checkValid[0])
                submitSlfcert();
            else {
                checkValid[1].focus();
                return false;
            }
        },
        2() {
            // 예: 모든 약관 체크 확인
            // const ok = [...document.querySelectorAll('[name="agree"]:checked')].length >= 3;
            // if(!ok){ alert('모든 약관에 동의해주세요.'); return false; }
            return true;
        },
        3() {
            // 예: 인증 완료 플래그 확인
            // if(!state.verified){ alert('본인인증을 완료하세요.'); return false; }
            return true;
        },
        4() {
            return true;
        },
        5() {
            return true;
        } // 제출 단계라면 서버 전송 등 처리
    };

    /* ============== step1 본인확인서 존재 여부 확인 ============== */
    (async function chkFATCAExist() {
        const wizard = document.getElementById('wizard');
        const mid = wizard.dataset.mid;
        const res = await fetch(`/BNK/api/slfcert/${mid}`, {method: 'HEAD'});
        if (res.ok) {
            wizard.setAttribute('data-has-info', 'true');
            showStep(2)
        }
        else {
            wizard.setAttribute('data-has-info', 'false');
            showStep(1);
        }
    })();

    /* ============== step1 유효성 검사 ============== */
    const form1 = document.getElementById('customerForm');
    const name = form1.name;
    const brthdt = form1.brthdt;
    const natcd = form1.natcd;
    const taxyr = form1.taxyr;
    const enlnm = form1.enlnm;
    const enfnm = form1.enfnm;
    const phone = form1.phone;
    const zipcd = form1.zipcd;
    const addr1 = form1.addr1;

    const validComment = (input, comment1, spanId, regEx = null, comment2 = null) => {
        input.addEventListener('focusout', function () {
            const span = document.getElementById(spanId);
            if (input.value === null || input.value.trim() === "") {
                span.innerText = comment1;
            } else if (regEx !== null) {
                if (!input.value.match(regEx))
                    span.innerText = comment2;
            } else
                span.innerText = "";
        });
    }
    validComment(name, '성명을 입력해주세요.', 'name-comment', reName, '성명이 유효하지 않습니다.');
    validComment(brthdt, '생년월일을 선택해주세요.', 'brthdt-comment');
    validComment(natcd, '국적을 선택해주세요.', 'natcd-comment');
    validComment(taxyr, '귀속년도를 선택해주세요.', 'taxyr-comment');
    validComment(enlnm, '영문성명을 입력해주세요.', 'ennm-comment');
    validComment(enfnm, '영문성명을 입력해주세요.', 'ennm-comment');
    validComment(phone, '전화번호를 입력해주세요.', 'phone-comment', reHp, '전화번호가 유효하지 않습니다.');
    validComment(zipcd, '우편번호와 주소를 입력해주세요.', 'addr-comment');
    validComment(addr1, '우편번호와 주소를 입력해주세요.', 'addr-comment');
    form1.addr2.addEventListener('focusout', function () {
        const comment = document.getElementById('addr-comment');
        if (zipcd.value !== "" && addr1.value !== "")
            comment.innerText = "";
        else {
            comment.innerText = "우편번호와 주소를 입력해주세요.";
        }
    });

    /* 화면 전환 */
    function showStep(n) {
        currentStep = n;
        pages.forEach((el, i) => el.hidden = (i !== n - 1));

        steps.forEach((li, i) => {
            li.classList.remove('active', 'done');
            li.removeAttribute('aria-current');
            if (i < n - 1) li.classList.add('done');
            if (i === n - 1) {
                li.classList.add('active');
                li.setAttribute('aria-current', 'step');
            }
        });

        // 이전 버튼: step1에서는 숨김, 그 외엔 표시
        // 본인확인서 중복 제출 방지를 위해 page2에서도 숨김
        prevBtn.hidden = (n === 1) || (n === 2);

        nextBtn.textContent = (n === totalSteps) ? '신청' : '다음';
        // 스크롤 보정
        document.querySelector('html').scrollIntoView({behavior: 'smooth', block: 'start'});
    }


    /* 다음/제출 */
    nextBtn.addEventListener('click', async () => {
        // 현재 단계 저장/검증
        if (!validators[currentStep]()) return;

        // 마지막이면 제출 동작
        if (currentStep === totalSteps) {
            // 예: 서버 제출 (fetch) or 확인 모달
            alert('제출했습니다!');
            window.location.href = "/BNK/product/subCmpl/list";
            return;
        }

        showStep(currentStep + 1);
    });

    // 본인확인서 제출 함수
    function submitSlfcert() {
        if (!getHasInfo() && currentStep === 1) {
            // 서버 제출 동작(fetch)
            const fd = new FormData(form1);
            fd.set('krres', form1.krres.checked ? 'Y' : 'N');
            fd.set('others', form1.others.checked ? 'Y' : 'N');
            fd.set('ftype', form1.natcd.value === 'US' ? 'W9' : 'W8');
            fd.set('sts', 'VALID');
            fetch('/BNK/api/slfcert', {
                method: 'POST',
                body: fd
            }).then(res => {
                if (res.ok)
                    return res.json();
                else if (res.status === 204)
                    return null;
                else
                    throw new Error(`${res.status} ${res.statusText}`);
            }).then(data => {
                console.log(data);
                alert('본인확인서(FATCA/CRS)가 등록되었습니다!');
                root.dataset.hasInfo = 'true';
                return true;
            }).catch(e => {
                console.error(e.message);
                alert('등록 중 오류가 발생했습니다.\n' + e.message);
                return false;
            });
        }
    }

    // 취소: 모든 단계에서 항상 동작
    document.getElementById('cancelBtn').addEventListener('click', () => {
        const ok = confirm('작성을 취소하고 이전 화면으로 돌아갈까요?\n저장되지 않은 내용은 사라질 수 있어요.');
        if (!ok) return;

        // 원하는 취소 정책으로 변경 가능
        if (history.length > 1) history.back();
        else location.href = '/';   // 또는 원하는 경로
    });

    // 이전
    prevBtn.addEventListener('click', () => {
        if (currentStep > 1) {
            if (!(getHasInfo() && currentStep === 2)) showStep(currentStep - 1);
        }
    });


    /* 스텝 클릭으로 점프 이동(선택) */
    steps.forEach((li, idx) => {
        li.style.cursor = 'pointer';
        li.addEventListener('click', () => {
            // 완료했거나 바로 이전까지만 허용 등 정책 가능
            if (idx + 1 <= currentStep || idx + 1 === currentStep + 1) {
                // 본인확인서 미등록시에만 step1 이동 가능
                if (!(getHasInfo() && idx === 0))
                    showStep(idx + 1);
            }
        });
    });

    /* 본인확인서(FATCA/CRS) 존재 여부 확인 */
    const $ = (sel, root = document) => root.querySelector(sel);
    const root = $('#wizard');

    function getHasInfo() {
        if (root && root.dataset.hasInfo != null) {
            return String(root.dataset.hasInfo).toLowerCase() === 'true';
        }
        return false;
    }

    /*============== 약관 및 상품설명서 받기 스크립트 ================*/
    const radios = document.querySelectorAll('input[name="receive"]');
    const input = document.getElementById('contactInput');
    const help = document.getElementById('contact-help');
    const error = document.getElementById('contact-error');

    function switchMode(mode) {
        input.value = '';
        error.textContent = '';
        if (mode === 'sms') {
            input.type = 'tel';
            input.placeholder = "휴대폰 번호 (‘-’ 없이)";
            input.setAttribute('inputmode', 'numeric');
            input.setAttribute('autocomplete', 'tel');
            input.setAttribute('pattern', '^01[0-9]{8,9}$');
            help.textContent = "휴대폰 번호는 ‘-’ 없이 숫자만 입력해 주세요.";
        } else {
            input.type = 'email';
            input.placeholder = "이메일 주소";
            input.removeAttribute('inputmode');
            input.setAttribute('autocomplete', 'email');
            input.removeAttribute('pattern');
            help.textContent = "정확한 이메일 주소를 입력해 주세요.";
        }
    }

    radios.forEach(r => r.addEventListener('change', e => switchMode(e.target.value)));
    switchMode(document.querySelector('input[name="receive"]:checked').value);

    // 간단한 실시간 유효성 안내
    input.addEventListener('blur', () => {
        if (!input.value) {
            error.textContent = '';
            return;
        }
        if (!input.checkValidity()) {
            error.textContent = (input.type === 'tel')
                ? '휴대폰 번호 형식이 올바르지 않습니다.'
                : '이메일 형식이 올바르지 않습니다.';
        } else {
            error.textContent = '';
        }
    });

    // 이 폼에서 엔터 제출 금지
    const termsForm = document.querySelector('.get-terms form');

    // 1) 기본 submit 자체 막기
    termsForm.addEventListener('submit', (e) => e.preventDefault());

    // 엔터 → 현재 포커스된 요소만 포커스 해제
    termsForm.addEventListener('keydown', (e) => {
        if ((e.key === 'Enter' || e.keyCode === 13) && !e.isComposing) {
            e.preventDefault();
            e.stopPropagation();
            const el = document.activeElement;
            if (el && typeof el.blur === 'function') el.blur();
        }
    });

    // (선택) 모바일 키보드 힌트만 ‘완료’로 바꾸기
    input.setAttribute('enterkeyhint', 'done');


    /* ================= 상품설명서 및 약관 다운로드 표시 스크립트 ===================*/
    // 신규가입(2단계) 약관 다운로드 완료 표시
    (function initTermsDownload() {
        const section = document.querySelector('#page2 .accept-terms');
        if (!section) return;

        const titleWrap = section.querySelector('.terms-title');
        const items = [...section.querySelectorAll('.terms-list .terms-item')];
        const links = items.map(i => i.querySelector('a.icon-btn')).filter(Boolean);

        // 전역 state 재사용 (스텝 이동시 상태 유지)
        window.state = window.state || {};
        state.termsDownloaded = state.termsDownloaded || {};

        // 진행률 표시 (간단 텍스트)
        const progress = document.createElement('span');
        progress.setAttribute('aria-live', 'polite');
        progress.style.marginLeft = '8px';
        progress.style.fontSize = '12px';
        progress.style.color = '#6B7280';
        titleWrap.querySelector('h2')?.appendChild(progress);

        function updateProgress() {
            const done = links.filter(a => a.classList.contains('downloaded')).length;
            progress.textContent = `(${done}/${links.length} 완료)`;
        }

        function markDownloaded(a, key) {
            if (a.classList.contains('downloaded')) return;
            a.classList.add('downloaded');                // ← CSS 활용
            a.setAttribute('aria-label', (a.getAttribute('aria-label') || '다운로드') + ' - 완료');
            a.setAttribute('data-downloaded', 'true');
            state.termsDownloaded[key] = true;
            updateProgress();
        }

        // 초기 복원
        links.forEach((a, idx) => {
            const key = a.closest('.terms-item')?.querySelector('span')?.textContent?.trim() || `term-${idx + 1}`;
            if (state.termsDownloaded[key]) a.classList.add('downloaded');
        });
        updateProgress();

        // 이벤트 바인딩: 클릭, Enter, Space
        links.forEach((a, idx) => {
            const key = a.closest('.terms-item')?.querySelector('span')?.textContent?.trim() || `term-${idx + 1}`;

            a.addEventListener('click', () => markDownloaded(a, key));
            a.addEventListener('keydown', (e) => {
                if (e.key === 'Enter' || e.key === ' ' || e.key === 'Spacebar') {
                    // a 요소는 기본 동작을 유지(다운로드), 완료만 표시
                    markDownloaded(a, key);
                }
            });
        });

        // (선택) 다음 단계로 넘어갈 때 모두 다운로드했는지 검증하고 싶다면 validators[2] 교체
        if (window.validators) {
            const original = validators[2] || (() => true);
            validators[2] = function () {
                // 기본 검증 통과 후 추가 체크
                const ok = links.every(a => a.classList.contains('downloaded'));
                if (!ok) {
                    alert('상품설명서 및 약관을 모두 다운로드(또는 열람)해 주세요.');
                    return false;
                }
                return original();
            };
        }
    })();


    /*================== 4단계 정보입력 스크립트 =====================*/

    // 숫자 포맷
    function formatNumber(v) {
        const n = String(v).replace(/[^\d]/g, '');
        if (!n) return '';
        return n.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }

    function setCurrencyInput(el) {
        el.addEventListener('input', () => {
            const pos = el.selectionStart;
            const beforeLen = el.value.length;
            el.value = formatNumber(el.value);
            // best-effort caret keep
            const afterLen = el.value.length;
            el.selectionEnd = el.selectionStart = pos + (afterLen - beforeLen);
        });
    }

    setCurrencyInput(document.getElementById('firstAmt'));
    setCurrencyInput(document.getElementById('goal'));

    // 최초불입금액 칩 동작
    const amtInput = document.getElementById('firstAmt');
    document.getElementById('firstAmtChips').addEventListener('click', (e) => {
        const btn = e.target.closest('.Chip');
        if (!btn) return;
        [...e.currentTarget.querySelectorAll('.Chip')].forEach(c => c.classList.remove('active'));
        btn.classList.add('active');
        const won = btn.getAttribute('data-won');
        if (won) {
            amtInput.value = formatNumber(won);
            amtInput.blur();
        } else {
            amtInput.focus();
            amtInput.select();
        }
    });

    // 계약기간 칩 동작
    const months = document.getElementById('termMonths');
    const termDate = document.getElementById('termDate');
    document.getElementById('termChips').addEventListener('click', (e) => {
        const chip = e.target.closest('.Chip');
        if (!chip) return;
        [...e.currentTarget.querySelectorAll('.Chip')].forEach(c => c.classList.remove('active'));
        chip.classList.add('active');
        const m = chip.dataset.month;
        const isDate = chip.dataset.type === 'date';
        if (isDate) {
            months.style.display = 'none';
            termDate.style.display = 'block';
            termDate.focus();
        } else {
            months.style.display = 'block';
            termDate.style.display = 'none';
            months.value = m || months.value;
            months.focus();
        }
    });

    // 개월 범위 보정
    months.addEventListener('change', () => {
        let v = parseInt(months.value || 0, 10);
        if (isNaN(v)) v = 6;
        v = Math.max(6, Math.min(36, v));
        months.value = v;
    });


    /*================== 5단계 pin 입력 스크립트 ==================*/

    // PIN 유틸
    function isSequentialOrRepeat(str) {
        if (str.length < 4) return false;
        // 반복(1111 등)
        if (/^(\d)\1{3}$/.test(str)) return true;
        // 오름차순 연속 0123, 4567, 6789
        if ('0123456789'.includes(str)) return true;
        // 내림차순 연속 9876, 3210
        if ('9876543210'.includes(str)) return true;
        return false;
    }

    // eye 아이콘 SVG
    function eyeSVG(open) {
        return open
            ? '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#6b7280" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12Z"/><circle cx="12" cy="12" r="3"/></svg>'
            : '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#6b7280" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m2 2 20 20"/><path d="M10.58 10.58A3 3 0 0 0 12 15a3 3 0 0 0 2.42-4.42"/><path d="M16.88 16.88C15.6 17.6 14 18 12 18 5 18 1 12 1 12a20.3 20.3 0 0 1 6.21-5.31"/><path d="M17.94 6.06A10.9 10.9 0 0 1 23 12s-1.64 2.64-4.12 4.54"/></svg>';
    }

    // PIN 그룹 세팅: 비밀번호 마스킹 + 자동이동 + 붙여넣기, (옵션) 표시/숨김 토글
    function setupPin(groupId, onFilled, {withToggle = false} = {}) {
        const wrap = document.getElementById(groupId);
        const inputs = [...wrap.querySelectorAll('input.pin')];

        inputs.forEach(inp => {
            inp.maxLength = 1;
            inp.inputMode = 'numeric';
            inp.autocomplete = 'off';
            inp.pattern = '[0-9]*';
            inp.type = 'password';                    // 기본 마스킹
            inp.setAttribute('enterkeyhint', 'done');  // 모바일 Enter 라벨 "완료"
        });

        let showAll = false;    // 눈아이콘 전체 표시 상태
        let visible = -1;       // 마지막으로 입력된 칸(보이는 칸)

        const isFilled = () => inputs.every(i => i.value && i.value.length === 1);

        function applyMasking() {
            if (showAll) {
                inputs.forEach(i => i.type = 'text');
                return;
            }
            inputs.forEach((i, idx) => i.type = (idx === visible ? 'text' : 'password'));
        }

        // 입력/키동작
        inputs.forEach((inp, idx) => {
            inp.addEventListener('input', e => {
                e.target.value = e.target.value.replace(/\D/g, '').slice(0, 1);
                if (e.target.value) {
                    visible = idx;          // 방금 입력한 칸만 보이기
                    applyMasking();

                    // 다음 칸으로 포커스 이동(이 시점엔 직전 칸 그대로 보임)
                    if (inputs[idx + 1]) inputs[idx + 1].focus();

                    // 모두 채워졌으면 콜백
                    const v = inputs.map(i => i.value).join('');
                    if (v.length === inputs.length) onFilled?.(v);
                }
            });

            inp.addEventListener('keydown', e => {
                // 백스페이스로 이전 칸으로 이동 시: 이전 칸만 보이기
                if (e.key === 'Backspace' && !inp.value && inputs[idx - 1]) {
                    inputs[idx - 1].focus();
                    visible = idx - 1;
                    applyMasking();
                }
                // 값이 있는데 숫자키 누르면 덮어쓰기
                if (/^\d$/.test(e.key) && inp.value) {
                    inp.value = '';
                }
            });

            // 포커스 이동만으로는 마스킹 변화 없음
            inp.addEventListener('focus', () => { /* no-op */
            });

            // 그룹을 벗어나면 안전하게 모두 마스킹
            inp.addEventListener('blur', () => {
                setTimeout(() => {
                    if (!wrap.contains(document.activeElement) && !showAll) {
                        visible = -1;
                        applyMasking();
                    }
                }, 0);
            });
        });

        // 🔸 Enter 처리: 4자리 다 찬 상태에서 Enter → 제출 막고 포커스 해제
        wrap.addEventListener('keydown', e => {
            if (e.key === 'Enter' && isFilled()) {
                e.preventDefault();
                e.stopPropagation();
                visible = -1;       // 모두 마스킹
                applyMasking();
                // 포커스 완전 해제
                const active = document.activeElement;
                if (wrap.contains(active)) active.blur();

                // (권장) 폼의 암묵적 제출도 한 번 더 차단
                const form = wrap.closest('form');
                if (form) {
                    const onceBlock = ev => {
                        ev.preventDefault();
                    };
                    form.addEventListener('submit', onceBlock, {once: true});
                    // 0ms 뒤 자동 제거(이번 엔터로 인한 submit만 막음)
                    setTimeout(() => form.removeEventListener('submit', onceBlock), 0);
                }
            }
        });

        // 붙여넣기: 마지막 채워진 칸만 보이기
        wrap.addEventListener('paste', e => {
            const txt = (e.clipboardData.getData('text') || '').replace(/\D/g, '').slice(0, inputs.length);
            if (!txt) return;
            e.preventDefault();
            inputs.forEach((i, k) => i.value = txt[k] || '');
            visible = Math.min(txt.length, inputs.length) - 1;
            (inputs[visible] || inputs[0]).focus();
            applyMasking();
            if (txt.length === inputs.length) onFilled?.(txt);
        });

        // (옵션) 눈 아이콘 토글
        if (withToggle) {
            const btn = document.createElement('button');
            btn.type = 'button';
            btn.className = 'pin-toggle';
            btn.setAttribute('aria-pressed', 'false');

            // 초기: 전체 숨김(showAll=false) → 감긴 눈 아이콘
            btn.innerHTML = eyeSVG(false);
            wrap.after(btn);

            btn.addEventListener('click', () => {
                showAll = !showAll;
                btn.setAttribute('aria-pressed', showAll ? 'true' : 'false');

                btn.innerHTML = eyeSVG(showAll);

                applyMasking();
            });
        }

        // 초기 상태
        visible = -1;
        applyMasking();

        return () => inputs.map(i => i.value).join('');
    }

    // 1차 PIN
    const pin1Get = setupPin('pin1', (v) => {
        const hint = document.getElementById('pin1Hint');
        if (isSequentialOrRepeat(v)) {
            hint.textContent = '연속되거나 반복되는 숫자는 사용할 수 없습니다.';
            hint.classList.add('error');
        } else {
            hint.textContent = '사용 가능한 비밀번호입니다.';
            hint.classList.remove('error');
        }
    });

    // 확인 PIN
    const pin2Get = setupPin('pin2', () => {
        const v1 = pin1Get(), v2 = pin2Get();
        if (v1 && v2 && v1 !== v2) {
            alert('신규추진 비밀번호가 일치하지 않습니다.');
        }
    });
});