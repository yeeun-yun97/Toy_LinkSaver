package com.github.yeeun_yun97.toy.linksaver.data

import com.github.yeeun_yun97.toy.linksaver.data.model.*
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjDomainListRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjLinkListRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjSearchSetListRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjTagListRepository

class SjTestDataUtil {

    companion object {
        val testDomain = listOf(
            SjDomain(
                did = 1,
                name = "-",
                url = ""
            ),
        )

        val testTagGroups = listOf(
            SjTagGroup(1, "_", false),
            SjTagGroup(2, "일반 그룹", false),
            SjTagGroup(3, "비밀 그룹", true),
        )
        val testTagGroupsNotDefault: List<SjTagGroup>
            get() {
                val result = mutableListOf<SjTagGroup>()
                for (group in testTagGroups) {
                    if (group.gid != 1) result.add(group)
                }
                return result
            }
        val testTagGroupsNotDefaultPublic: List<SjTagGroup>
            get() {
                val result = mutableListOf<SjTagGroup>()
                for (group in testTagGroups) {
                    if (group.gid != 1 && !group.isPrivate) result.add(group)
                }
                return result
            }

        val testTags = listOf(
            SjTag(1, "기본", gid = 1),
            SjTag(2, "기본2", gid = 1),
            SjTag(3, "기본3", gid = 1),
            SjTag(4, "일반", gid = 2),
            SjTag(5, "일반2", gid = 2),
            SjTag(6, "비밀", gid = 3),
            SjTag(7, "비밀2", gid = 3),
            SjTag(8, "비밀3", gid = 3),
        )

        val testTagsDefault: List<SjTag>
            get() {
                val result = mutableListOf<SjTag>()
                for (tag in testTags) {
                    if (tag.gid == 1) result.add(tag)
                }
                return result
            }

        val testTagsNotDefault: List<SjTag>
            get() {
                val result = mutableListOf<SjTag>()
                for (tag in testTags) {
                    if (tag.gid != 1) result.add(tag)
                }
                return result
            }

        val testTagsPublic: List<SjTag>
            get() {
                val result = mutableListOf<SjTag>()
                for (tag in testTags) {
                    if (tag.gid != 1)
                        for (group in testTagGroupsNotDefaultPublic) {
                            if (tag.gid == group.gid) result.add(tag)
                        }
                    else result.add(tag)
                }
                return result
            }

        val testTidsPublic: List<Int>
            get() {
                val result = mutableListOf<Int>()
                for (tag in testTagsPublic) {
                    result.add(tag.tid)
                }
                return result
            }

        val testLinks = listOf(
            Pair(
                SjLink(
                    lid = 1, did = 1,
                    name = "네이버", url = "https://www.naver.com/",
                    type = ELinkType.link
                ), listOf(testTags[1], testTags[0])
            ),
            Pair(
                SjLink(
                    lid = 2, did = 1,
                    name = "유투브", url = "https://www.youtube.com/",
                    type = ELinkType.video
                ), listOf(testTags[0], testTags[2])
            ),
            Pair(
                SjLink(
                    lid = 3, did = 1,
                    name = "네이버_비밀", url = "https://www.naver.com/",
                    type = ELinkType.link
                ), listOf(testTags[6])
            ),
            Pair(
                SjLink(
                    lid = 4, did = 1,
                    name = "유투브_비밀", url = "https://www.youtube.com/",
                    type = ELinkType.video
                ), listOf(testTags[1], testTags[5])
            ),


            Pair(
                SjLink(
                    lid = 5, did = 1,
                    name = "검색용 링크", url = "https://www.youtube.com/",
                    type = ELinkType.video
                ), listOf(testTags[0], testTags[1], testTags[3])
            ),
            Pair(
                SjLink(
                    lid = 6, did = 1,
                    name = "이 링크를 검색하세요", url = "https://www.youtube.com/",
                    type = ELinkType.link
                ), listOf(testTags[0], testTags[3])
            ),
            Pair(
                SjLink(
                    lid = 7, did = 1,
                    name = "비밀용 검색", url = "https://www.youtube.com/",
                    type = ELinkType.video
                ), listOf(testTags[0], testTags[3], testTags[5])
            ),
        )

        val testLinksPublic: List<Pair<SjLink, List<SjTag>>>
            get() {
                val result = mutableListOf<Pair<SjLink, List<SjTag>>>()
                for (link in testLinks) {
                    if (testTagsPublic.containsAll(link.second))
                        result.add(link)
                }
                return result
            }

        val testVideoLinks: List<SjLink>
            get() {
                val result = mutableListOf<SjLink>()
                for (link in testLinks) {
                    if (link.first.type == ELinkType.video)
                        result.add(link.first)
                }
                return result
            }

        val testVideoLinksPublic: List<SjLink>
            get() {
                val result = mutableListOf<SjLink>()
                for (link in testLinksPublic) {
                    if (link.first.type == ELinkType.video)
                        result.add(link.first)
                }
                return result
            }

        val testSearchSets = listOf(
            Pair(SjSearch(1, "유투브"), listOf(1)),
            Pair(SjSearch(2, "비밀"), listOf(1, 2)),
            Pair(SjSearch(3, "네이버"), listOf(1)),
            Pair(SjSearch(4, "유네이버투브"), listOf(0)),
        )

        val testSearchSetsPublic: List<Pair<SjSearch, List<Int>>>
            get() {
                val result = mutableListOf<Pair<SjSearch, List<Int>>>()
                for (search in testSearchSets) {
                    if (testTidsPublic.containsAll(search.second)) {
                        result.add(search)
                    }
                }
                return result
            }

        suspend fun insertDatas(
            linkRepo: SjLinkListRepository,
            domainRepo: SjDomainListRepository,
            tagRepo: SjTagListRepository,
            searchSetRepo: SjSearchSetListRepository
        ) {
            for (search in testSearchSets) searchSetRepo.insertSearchSet(
                search.first.sid,
                search.first.keyword,
                search.second
            )
            for (domain in testDomain) domainRepo.insertDomain(domain)
            for (group in testTagGroups)
                tagRepo.insertTagGroup(
                    gid = group.gid,
                    name = group.name,
                    isPrivate = group.isPrivate
                ).join()
            for (tag in testTags) tagRepo.insertTag(tag.tid, tag.name, tag.gid).join()
            for (link in testLinks) linkRepo.insertLinkAndTags(null, link.first, link.second)
                .join()

        }
    }
}