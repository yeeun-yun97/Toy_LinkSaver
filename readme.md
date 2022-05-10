# LinkTag
## 개요
- 태그기반으로 URL을 관리하는 Application
### 버전 v.1.1  <a href ="https://play.google.com/store/apps/details?id=com.github.yeeun_yun97.toy.linksaver" target = "_blank" >플레이스토어</a>
log
- 기본 도메인 이름 변경 및 숨김
- 검색에서 태그를 선택하지 않았을 때 결과가 정상적으로 나오지 않는 문제 해결
- 검색 아이콘에 검색 시작 기능 및 검색 취소 기능 추가
- 칩 UI 약간 수정 (체크 아이콘 제거 및 색 테마 적용)
- 검색모드에서도 링크 추가 시 바로 리스트에 표시하도록 수정   

## 기간
- 2022.04.XX ~

## 스택
<img src="https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=Android&logoColor=black"/> <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=Kotlin&logoColor=black"/> 

## 특징
- Kotlin + MVVM + LiveData + Room
- DataBinding
- 링크, 태그, 도메인 추가/삭제/수정
- 태그 기반으로 링크 검색
- 자주 재사용 할 만한 부분들을 Android Library로 따로 관리

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

## 내부 설계 소개
### Room DB
![스크린샷 2022-05-10 오후 11 38 32](https://user-images.githubusercontent.com/60867063/167655000-a493e766-a765-44f7-beb4-f72823bbba2b.png)    
### Architecture
![스크린샷 2022-05-10 오후 11 36 50](https://user-images.githubusercontent.com/60867063/167654673-62be891a-7719-4ea7-a357-83caaa173618.png)    

<!--
## 기능 소개 - 태그 관리
### :: 태그 생성하기 ::
![tag_create](https://user-images.githubusercontent.com/60867063/166867419-d60935ff-d039-4a20-9057-40e08ceac970.gif) 
### :: 태그 수정하기 ::
![tag_update](https://user-images.githubusercontent.com/60867063/166866973-dc5ad60c-ab29-476d-9963-24b2049f9d4b.gif)
### :: 태그 삭제하기 ::
![tag_delete](https://user-images.githubusercontent.com/60867063/166867693-1c9b59b2-8d94-4dca-9c1c-3df2f2cd0952.gif)
    
## 기능 소개 - 도메인 관리
### :: 도메인 생성하기 ::
![domain_create](https://user-images.githubusercontent.com/60867063/166867190-37ea4d82-f543-4b19-966f-a6b732628ff0.gif)
### :: 도메인 수정하기 ::
![domain_update](https://user-images.githubusercontent.com/60867063/166874717-2ca283a6-8c32-4c6a-9cf0-514655c390fa.gif)
### :: 도메인 삭제하기 ::
![domain_delete](https://user-images.githubusercontent.com/60867063/166874941-89849cf8-bd79-44fd-a713-46853ebe2fa7.gif)
    
## 기능 소개 - 링크 관리
### :: 링크 자동으로 이름 생성하기 ::
![autotitle](https://user-images.githubusercontent.com/60867063/166875761-f8c6ebf0-69ab-4ac8-9c36-144030e3b577.gif)
### :: 링크 생성하기 ::
![link_create](https://user-images.githubusercontent.com/60867063/166876018-a1b5ebcf-30c3-4ab0-838a-dc33fcfdc3b1.gif)
### :: 링크 수정하기 ::
![link_update](https://user-images.githubusercontent.com/60867063/166876047-a4e696cd-874c-47d6-bcc2-742b099e5fe3.gif)
### :: 링크 삭제하기 ::
![link_delete](https://user-images.githubusercontent.com/60867063/166876064-2bf9f07e-9bb0-4097-928c-07a2c5c568da.gif)

## 기능 소개 - 링크 검색
### :: 링크 검색하기(검색어) ::
![search_name](https://user-images.githubusercontent.com/60867063/166876082-7c5d1593-ab55-49f3-8336-2266cc635b0a.gif)
### :: 링크 검색하기(태그선택) ::
![search_tag](https://user-images.githubusercontent.com/60867063/166876225-c72476fd-ebe5-4ec7-af53-d8b1a7e26be3.gif)
-->
