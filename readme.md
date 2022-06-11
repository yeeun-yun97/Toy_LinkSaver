# LinkTag 링크택
## 개요
- 태그기반으로 URL을 관리하는 Application
- (시중의 비슷한 앱들은 디렉토리 방식으로 관리 -> 불편함을 태그 방식으로 해결하자.)
### 버전 v 2.2  <a href ="https://play.google.com/store/apps/details?id=com.github.yeeun_yun97.toy.linksaver" target = "_blank" >플레이스토어</a> 
log
- 데이터가 없을 때 안내 UI가 추가되었습니다. (동영상 목록, 태그 그룹, 태그)
- 상세 보기 화면에서 썸네일을 눌러도 브라우저가 열리도록 변경하였습니다.
- 동영상 목록 화면의 스크롤이 초기화되지 않는 문제를 해결하였습니다.
- 검색 결과 화면에서 검색 취소하고 다시 전체를 보는 버튼을 추가하였습니다.
### 미리보기<a href ="https://github.com/yeeun-yun97/Toy_LinkSaver/blob/main/preview.md"> preview.md </a>


## 기간
- 2022.04.03 ~

## 스택
<img src="https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=Android&logoColor=black"/> <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=Kotlin&logoColor=black"/> 

## 특징
- Kotlin + MVVM + LiveData + Room
- DataBinding
- 링크, 태그, 도메인 추가/삭제/수정
- 태그 기반으로 링크 검색
- 자주 재사용 할 만한 부분들을 Android Library로 따로 관리
- Jira 이슈 관리
- 링크(동영상) 목록으로 미리보기

## skills
- Room
- LiveData
- ViewModel
- dataBinding
- viewBinding
- lifecycle
- okhttp3 
- jsoup
- coroutine
- junit
- exoplayer

## 내부 설계 소개
### Room DB
![스크린샷 2022-06-07 오후 10 53 23](https://user-images.githubusercontent.com/60867063/172397898-da21ace0-6b60-4d36-ad4b-b2351ae73447.png)

### Architecture
![스크린샷 2022-05-10 오후 11 36 50](https://user-images.githubusercontent.com/60867063/167654673-62be891a-7719-4ea7-a357-83caaa173618.png)    
