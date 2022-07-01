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

        val testTags = listOf(
            SjTag(1, "기본", gid = 1),
            SjTag(2, "일반", gid = 2),
            SjTag(3, "비밀", gid = 3)
        )

        val testAllLinks = listOf(
            Pair(
                SjLink(
                    lid = 1,
                    name = "네이버",
                    url = "https://www.naver.com/",
                    did = 1,
                    type = ELinkType.link
                ), listOf(testTags[1], testTags[0])
            ),
            Pair(
                SjLink(
                    lid = 2,
                    name = "유투브",
                    url = "https://www.youtube.com/",
                    did = 1,
                    type = ELinkType.video
                ), listOf(testTags[0], testTags[2])
            ),
            Pair(
                SjLink(
                    lid = 3,
                    name = "네이버_비밀",
                    url = "https://www.naver.com/",
                    did = 1,
                    type = ELinkType.link
                ), listOf(testTags[0])
            ),
            Pair(
                SjLink(
                    lid = 4,
                    name = "유투브_비밀",
                    url = "https://www.youtube.com/",
                    did = 1,
                    type = ELinkType.video
                ), listOf(testTags[1], testTags[2])
            ),
        )

        val testLinksPublic = listOf(
            testAllLinks[0],
            testAllLinks[2]
        )

        val testSearchSets = listOf(
            Pair(SjSearch(1, "유투브"), listOf(1)),
            Pair(SjSearch(2, "비밀"), listOf(1)),
            Pair(SjSearch(3, "네이버"), listOf(1)),
            Pair(SjSearch(4, "유네이버투브"), listOf(0)),
        )

        suspend fun insertDatas(
            linkRepo: SjLinkRepository,
            domainRepo: SjDomainRepository,
            tagRepo: SjTagRepository,
            searchSetRepo: SjSearchSetRepository
        ) {

            for (domain in testDomain) domainRepo.insertDomain(domain)
            for (group in testTagGroups)
                tagRepo.insertTagGroup(
                    gid = group.gid,
                    name = group.name,
                    isPrivate = group.isPrivate
                )
            for (tag in testTags) tagRepo.insertTag(tag.tid, tag.name, tag.gid).join()
            for (link in testAllLinks) linkRepo.insertLinkAndTags(null, link.first, link.second)
                .join()
            for (search in testSearchSets) searchSetRepo.insertSearchSet(
                search.first.sid,
                search.first.keyword,
                search.second
            )
        }
    }
}