package com.github.yeeun_yun97.toy.linksaver.data

import com.github.yeeun_yun97.toy.linksaver.data.model.*
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjDomainRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjLinkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjSearchSetRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjTagRepository

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

        val testTagGroupsNotDefault = listOf(
            testTagGroups[1],
            testTagGroups[2]
        )

        val testTagGroupsNotDefaultPublic = listOf(
            testTagGroups[2]
        )

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

        val testTagsDefault = listOf(
            testTags[0],
            testTags[1],
            testTags[2],
        )

        val testTagsNotDefault = listOf(
            testTags[3],
            testTags[4],
            testTags[5],
            testTags[6],
            testTags[7]
        )

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

        val testLinksPublic = listOf(
            testLinks[0],
            testLinks[1],
            testLinks[4],
            testLinks[5]
        )

        val testSearchSets = listOf(
            Pair(SjSearch(1, "유투브"), listOf(1)),
            Pair(SjSearch(2, "비밀"), listOf(1, 2)),
            Pair(SjSearch(3, "네이버"), listOf(1)),
            Pair(SjSearch(4, "유네이버투브"), listOf(0)),
        )

        val testSearchSetsPublic = listOf(
            testSearchSets[0],
            testSearchSets[2],
            testSearchSets[3],
        )

        suspend fun insertDatas(
            linkRepo: SjLinkRepository,
            domainRepo: SjDomainRepository,
            tagRepo: SjTagRepository,
            searchSetRepo: SjSearchSetRepository
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