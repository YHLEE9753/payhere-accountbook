# payhere-accountbook
## 실행방법
1. [구글드라이브](https://drive.google.com/drive/folders/11nl_p-KfaZ_MoOgBteQo-aWzSKplNccm?usp=sharing)에 접속한 후 `docker-compose.yml` 를 다운로드한다.
2. 다운로드 디렉토리에서 cmd 접속 후 `docker-compose up -d` 명령어를 실행한다.
3. MySQL 포트 설정 : `3306`, Spring 포트 설정 : `8080` - 해당 포트를 사용중이라면 정지후 재실행한다.
4. `http://localhost:8080` 에 접속한다.

### 개발
feat1 (220924-220925) : 공통 설정 추가(JPA Audit, Error 핸들링, Security), JWT 토큰 추가, AccessToken, RefreshToken 기반의 인증 filter 작성
