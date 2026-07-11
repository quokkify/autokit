# Changelog

## 1.0.0 (2026-07-11)


### Features

* add module with Selenide ([#51](https://github.com/ylazakovich/quokkify/issues/51)) ([1caa9ce](https://github.com/ylazakovich/quokkify/commit/1caa9ce86f8d32fcb9a2eab7b868eab780d8ee0a))
* add Tyrus WebSocket test module ([#195](https://github.com/ylazakovich/quokkify/issues/195)) ([315fd24](https://github.com/ylazakovich/quokkify/commit/315fd249195e47a5b3e8069d5ac3d5cab0e245c0))
* **ci:** prefer self-hosted runner with safe fallback ([#115](https://github.com/ylazakovich/quokkify/issues/115)) ([078dc51](https://github.com/ylazakovich/quokkify/commit/078dc516833421355f8d35f1f338545691a5f02d))
* configure ci to use concurrent jobs (1 module per 1 runner) ([#98](https://github.com/ylazakovich/quokkify/issues/98)) ([1828d89](https://github.com/ylazakovich/quokkify/commit/1828d896b452dd7e7d4a9f8014e1be8627493020))
* create config for TestNG extension ([#107](https://github.com/ylazakovich/quokkify/issues/107)) ([c76952e](https://github.com/ylazakovich/quokkify/commit/c76952e2cdbca2d6b8cce9c83759ce7096229c7c))
* fluent verify().withTimeout().withPolling() chain across all modules ([#252](https://github.com/ylazakovich/quokkify/issues/252)) ([086559e](https://github.com/ylazakovich/quokkify/commit/086559eb7c844eea67a62f39ea2d9133eaa058c5))
* port Group 1 improvements ([#191](https://github.com/ylazakovich/quokkify/issues/191)) ([5efd161](https://github.com/ylazakovich/quokkify/commit/5efd161adf09b0ba47eb1b65519bc9fa9dd27927))
* **reportportal:** decouple TMS integration via SPI (closes [#165](https://github.com/ylazakovich/quokkify/issues/165)) ([#247](https://github.com/ylazakovich/quokkify/issues/247)) ([f8126d4](https://github.com/ylazakovich/quokkify/commit/f8126d4b604734ce02391072e353969a40ef6c6f))
* **reportportal:** expand test coverage — unit + integration tests (closes [#166](https://github.com/ylazakovich/quokkify/issues/166)) ([#237](https://github.com/ylazakovich/quokkify/issues/237)) ([a3d421e](https://github.com/ylazakovich/quokkify/commit/a3d421eaaa44f38c19b4ffcaac682677925c29f0))
* selenide separate modules ([#101](https://github.com/ylazakovich/quokkify/issues/101)) ([#201](https://github.com/ylazakovich/quokkify/issues/201)) ([84d3c01](https://github.com/ylazakovich/quokkify/commit/84d3c010e00572e8bc74bd3e21891db314409a5c))


### Bug Fixes

* close connection when max_response_time is exceeded in rest-assured ([#202](https://github.com/ylazakovich/quokkify/issues/202)) ([ab042d0](https://github.com/ylazakovich/quokkify/commit/ab042d09d27f2794c79489f77b388a0dbde95cdf))
* **deps:** update allure to v2.32.0 ([#86](https://github.com/ylazakovich/quokkify/issues/86)) ([b7edb05](https://github.com/ylazakovich/quokkify/commit/b7edb0553a0393dfad6d7229908223e65da4456e))
* **deps:** update allure to v2.34.0 ([#200](https://github.com/ylazakovich/quokkify/issues/200)) ([8210750](https://github.com/ylazakovich/quokkify/commit/8210750822661a45107155803d674df2b865e541))
* **deps:** update allure to v2.35.2 ([#291](https://github.com/ylazakovich/quokkify/issues/291)) ([1aa81db](https://github.com/ylazakovich/quokkify/commit/1aa81dbb88f342267a50ed856f195aef3ce62c53))
* **deps:** update allure to v2.35.3 ([#321](https://github.com/ylazakovich/quokkify/issues/321)) ([4627a94](https://github.com/ylazakovich/quokkify/commit/4627a941543e2967e85b98ec3ab794bad6e985db))
* **deps:** update checkstyle to v12.1.1 ([#63](https://github.com/ylazakovich/quokkify/issues/63)) ([a0c5cf7](https://github.com/ylazakovich/quokkify/commit/a0c5cf730b8fa7add18883d1077c07ca87be189b))
* **deps:** update checkstyle to v12.1.2 ([#71](https://github.com/ylazakovich/quokkify/issues/71)) ([9aa9338](https://github.com/ylazakovich/quokkify/commit/9aa9338bf81370eeefd16aa2a5e612807564a6b7))
* **deps:** update checkstyle to v12.3.0 ([#87](https://github.com/ylazakovich/quokkify/issues/87)) ([56ea494](https://github.com/ylazakovich/quokkify/commit/56ea494aa36cfa8814235f43690fce8499234adf))
* **deps:** update checkstyle to v13 ([#110](https://github.com/ylazakovich/quokkify/issues/110)) ([d8779b6](https://github.com/ylazakovich/quokkify/commit/d8779b6709661121a69860e413259e3e4450cefa))
* **deps:** update checkstyle to v13.1.0 ([#141](https://github.com/ylazakovich/quokkify/issues/141)) ([faa06ff](https://github.com/ylazakovich/quokkify/commit/faa06ff362d1923ceec141411678a3c02bd8ee55))
* **deps:** update checkstyle to v13.2.0 ([#155](https://github.com/ylazakovich/quokkify/issues/155)) ([1347ad9](https://github.com/ylazakovich/quokkify/commit/1347ad9cb6e1a462a8e3b2424ee0fb7a08274640))
* **deps:** update checkstyle to v13.4.2 ([#234](https://github.com/ylazakovich/quokkify/issues/234)) ([fda854c](https://github.com/ylazakovich/quokkify/commit/fda854ce57d3bfa8348c1cd18f33744d1d0641e1))
* **deps:** update checkstyle to v13.6.0 ([#292](https://github.com/ylazakovich/quokkify/issues/292)) ([c69a9a4](https://github.com/ylazakovich/quokkify/commit/c69a9a4e105f4e0c10bb88e4a7a52c20d87fe4ad))
* **deps:** update checkstyle to v13.7.0 ([#306](https://github.com/ylazakovich/quokkify/issues/306)) ([0d8245c](https://github.com/ylazakovich/quokkify/commit/0d8245c428f7c8c20d11f33085d37e2ad58aa37e))
* **deps:** update com.fasterxml.jackson:jackson-bom to v2.20.1 ([#58](https://github.com/ylazakovich/quokkify/issues/58)) ([55714db](https://github.com/ylazakovich/quokkify/commit/55714db185e55991721a5a7fe81e20849dac0bd2))
* **deps:** update com.fasterxml.jackson:jackson-bom to v2.21.0 ([#119](https://github.com/ylazakovich/quokkify/issues/119)) ([6ba75c2](https://github.com/ylazakovich/quokkify/commit/6ba75c215c91a5a7ce9c0c56470c42b3f960df85))
* **deps:** update com.fasterxml.jackson:jackson-bom to v2.21.3 ([#182](https://github.com/ylazakovich/quokkify/issues/182)) ([925be24](https://github.com/ylazakovich/quokkify/commit/925be24f0aae2cd8f6294cb5a14418b5f0b478a6))
* **deps:** update com.fasterxml.jackson:jackson-bom to v2.22.0 ([#289](https://github.com/ylazakovich/quokkify/issues/289)) ([1ca183b](https://github.com/ylazakovich/quokkify/commit/1ca183bf1e70eddaf0f2060032bfdb408d078b56))
* **deps:** update com.fasterxml.jackson:jackson-bom to v2.22.1 ([#322](https://github.com/ylazakovich/quokkify/issues/322)) ([6774851](https://github.com/ylazakovich/quokkify/commit/67748510d183c3e35e2f8c90593395b56bbfa8c4))
* **deps:** update com.github.spotbugs:spotbugs-annotations to v4.10.2 ([#297](https://github.com/ylazakovich/quokkify/issues/297)) ([5832c4a](https://github.com/ylazakovich/quokkify/commit/5832c4a8d7fe42501178b0b34493dc909a6f3119))
* **deps:** update com.github.spotbugs:spotbugs-annotations to v4.9.8 ([#59](https://github.com/ylazakovich/quokkify/issues/59)) ([69f3e94](https://github.com/ylazakovich/quokkify/commit/69f3e944244b5ab7378e667f260ee10286796a57))
* **deps:** update com.google.guava:guava to v33.6.0-jre ([#196](https://github.com/ylazakovich/quokkify/issues/196)) ([63ede88](https://github.com/ylazakovich/quokkify/commit/63ede883c34676d0386787adfcbe810b8118332b))
* **deps:** update com.rabbitmq:amqp-client to v5.30.0 ([#232](https://github.com/ylazakovich/quokkify/issues/232)) ([00313ca](https://github.com/ylazakovich/quokkify/commit/00313caf73085e18662b6e56245c55553c9d2910))
* **deps:** update com.rabbitmq:amqp-client to v5.31.0 ([#295](https://github.com/ylazakovich/quokkify/issues/295)) ([742ab2d](https://github.com/ylazakovich/quokkify/commit/742ab2d27f1bd57ef75efcd1262b2130cf759e1e))
* **deps:** update com.rabbitmq:amqp-client to v5.33.0 ([#312](https://github.com/ylazakovich/quokkify/issues/312)) ([59d0324](https://github.com/ylazakovich/quokkify/commit/59d032452e6aa6f3644a9968a696b031f6fb55c6))
* **deps:** update com.rabbitmq:amqp-client to v5.33.1 ([#323](https://github.com/ylazakovich/quokkify/issues/323)) ([e5fc2b8](https://github.com/ylazakovich/quokkify/commit/e5fc2b8b13e089bac1eccdf9bccab38d6f106a00))
* **deps:** update com.rabbitmq:amqp-client to v5.34.0 ([#336](https://github.com/ylazakovich/quokkify/issues/336)) ([d6ac7f9](https://github.com/ylazakovich/quokkify/commit/d6ac7f9d617907ae343b2d6dc9fc7f5c06df9fa8))
* **deps:** update commons-io:commons-io to v2.21.0 ([#68](https://github.com/ylazakovich/quokkify/issues/68)) ([6074469](https://github.com/ylazakovich/quokkify/commit/6074469eb041c51b36714afa5716ff4c6ceda6bb))
* **deps:** update commons-io:commons-io to v2.22.0 ([#188](https://github.com/ylazakovich/quokkify/issues/188)) ([e0cd290](https://github.com/ylazakovich/quokkify/commit/e0cd29080346824988ffa087656a4d71bbbb92b0))
* **deps:** update dev.morphia.morphia:morphia-core to v2.5.3 ([#258](https://github.com/ylazakovich/quokkify/issues/258)) ([80d37f6](https://github.com/ylazakovich/quokkify/commit/80d37f66f256553fcc621047b0ab0777afcf96a2))
* **deps:** update feign monorepo to v13.12 ([#198](https://github.com/ylazakovich/quokkify/issues/198)) ([0ede68c](https://github.com/ylazakovich/quokkify/commit/0ede68c2f38c0efabdcb2320856ca35ab3e362d0))
* **deps:** update feign monorepo to v13.13 ([#311](https://github.com/ylazakovich/quokkify/issues/311)) ([e808ba4](https://github.com/ylazakovich/quokkify/commit/e808ba4a174a131140f48b472791e2f7f0f4b3bd))
* **deps:** update feign monorepo to v13.8 ([#163](https://github.com/ylazakovich/quokkify/issues/163)) ([fe936e0](https://github.com/ylazakovich/quokkify/commit/fe936e04576821d4c2ece74957cc827f9fb07c8f))
* **deps:** update hibernate-orm monorepo to v7.1.6.final ([#60](https://github.com/ylazakovich/quokkify/issues/60)) ([7214a16](https://github.com/ylazakovich/quokkify/commit/7214a16863f28abcd714240c47cb6fe6a6b19f6e))
* **deps:** update hibernate-orm monorepo to v7.1.7.final ([#66](https://github.com/ylazakovich/quokkify/issues/66)) ([4bc214c](https://github.com/ylazakovich/quokkify/commit/4bc214c29854a0536380a898d546d06aa2a67643))
* **deps:** update hibernate-orm monorepo to v7.2.0.final ([#88](https://github.com/ylazakovich/quokkify/issues/88)) ([b96eb57](https://github.com/ylazakovich/quokkify/commit/b96eb57639ab7a2d3ccb5801e4c4b77b3cbed57e))
* **deps:** update hibernate-orm monorepo to v7.2.1.final ([#111](https://github.com/ylazakovich/quokkify/issues/111)) ([bf670be](https://github.com/ylazakovich/quokkify/commit/bf670bef5ce99ce905174707b83dec54b225faec))
* **deps:** update hibernate-orm monorepo to v7.2.2.final ([#124](https://github.com/ylazakovich/quokkify/issues/124)) ([b7e501e](https://github.com/ylazakovich/quokkify/commit/b7e501ee68f6c98a677f742b1ab0bb937b694754))
* **deps:** update hibernate-orm monorepo to v7.2.4.final ([#144](https://github.com/ylazakovich/quokkify/issues/144)) ([fd30fc7](https://github.com/ylazakovich/quokkify/commit/fd30fc7f46162e8e465441949da72bf0d7c89843))
* **deps:** update hibernate-orm monorepo to v7.3.3.final ([#199](https://github.com/ylazakovich/quokkify/issues/199)) ([187bb51](https://github.com/ylazakovich/quokkify/commit/187bb51e90f2d4af56a8c35f5cf6d8b5f7761f44))
* **deps:** update hibernate-orm monorepo to v7.3.5.final ([#253](https://github.com/ylazakovich/quokkify/issues/253)) ([81e0842](https://github.com/ylazakovich/quokkify/commit/81e084283f10f35eb378158fc172c36b8b436190))
* **deps:** update hibernate-orm monorepo to v7.3.6.final ([#276](https://github.com/ylazakovich/quokkify/issues/276)) ([9ee1583](https://github.com/ylazakovich/quokkify/commit/9ee158307a4b8afaaeaa15e62c1318b05cd7c28c))
* **deps:** update hibernate-orm monorepo to v7.4.2.final ([#305](https://github.com/ylazakovich/quokkify/issues/305)) ([a3865c2](https://github.com/ylazakovich/quokkify/commit/a3865c2f927ac4f243549c9698da94480afcfbfa))
* **deps:** update hibernate-orm monorepo to v7.4.3.final ([#328](https://github.com/ylazakovich/quokkify/issues/328)) ([efe379b](https://github.com/ylazakovich/quokkify/commit/efe379b07f2f9eb9edbf187945f15b41ee9b7090))
* **deps:** update hibernate-orm monorepo to v7.4.4.final ([#335](https://github.com/ylazakovich/quokkify/issues/335)) ([e8c1ba2](https://github.com/ylazakovich/quokkify/commit/e8c1ba26456430994bd746e76f856903d8e9fd01))
* **deps:** update io.atlassian.fugue:fugue to v6.1.2 ([#145](https://github.com/ylazakovich/quokkify/issues/145)) ([b014be3](https://github.com/ylazakovich/quokkify/commit/b014be390a14b4a5ff2f4b2aaa6214b0f6d73058))
* **deps:** update io.atlassian.fugue:fugue to v6.1.3 ([#183](https://github.com/ylazakovich/quokkify/issues/183)) ([03bb6d2](https://github.com/ylazakovich/quokkify/commit/03bb6d23876c7d25849a92d6526055b40bfe55af))
* **deps:** update io.atlassian.fugue:fugue to v6.1.4 ([#259](https://github.com/ylazakovich/quokkify/issues/259)) ([98a44cf](https://github.com/ylazakovich/quokkify/commit/98a44cf249cb94e228f20e70d11bb19c23fa6d19))
* **deps:** update io.atlassian.fugue:fugue to v6.1.5 ([#315](https://github.com/ylazakovich/quokkify/issues/315)) ([b586268](https://github.com/ylazakovich/quokkify/commit/b5862689ec476d1901272b02153b6c0c18e9576b))
* **deps:** update io.hypersistence:hypersistence-utils-hibernate-71 to v3.12.0 ([#74](https://github.com/ylazakovich/quokkify/issues/74)) ([f8b2c97](https://github.com/ylazakovich/quokkify/commit/f8b2c97c067ab558427a72d6d8df6c01b382ad04))
* **deps:** update io.hypersistence:hypersistence-utils-hibernate-71 to v3.13.3 ([#89](https://github.com/ylazakovich/quokkify/issues/89)) ([4db8235](https://github.com/ylazakovich/quokkify/commit/4db823500e1a2c7317c52c2429d992f38b8f5252))
* **deps:** update io.hypersistence:hypersistence-utils-hibernate-71 to v3.14.0 ([#99](https://github.com/ylazakovich/quokkify/issues/99)) ([552d127](https://github.com/ylazakovich/quokkify/commit/552d12787c4654a8230bcc29e0dff8e8d85e093c))
* **deps:** update io.hypersistence:hypersistence-utils-hibernate-71 to v3.14.1 ([#109](https://github.com/ylazakovich/quokkify/issues/109)) ([93c3879](https://github.com/ylazakovich/quokkify/commit/93c387986cb58d423c37c4c0dad8c0b77f7666e4))
* **deps:** update io.hypersistence:hypersistence-utils-hibernate-71 to v3.15.1 ([#126](https://github.com/ylazakovich/quokkify/issues/126)) ([d972221](https://github.com/ylazakovich/quokkify/commit/d972221c4eac8ba2cd17e859c75fd0a68311cf56))
* **deps:** update io.hypersistence:hypersistence-utils-hibernate-71 to v3.15.2 ([#153](https://github.com/ylazakovich/quokkify/issues/153)) ([c53e8fd](https://github.com/ylazakovich/quokkify/commit/c53e8fdb50e785bbc92a94ecf47395f587f9ae42))
* **deps:** update io.hypersistence:hypersistence-utils-hibernate-73 to v3.15.3 ([#288](https://github.com/ylazakovich/quokkify/issues/288)) ([a5560b1](https://github.com/ylazakovich/quokkify/commit/a5560b13359dd8508f2ec90a9558c76dfe8406f4))
* **deps:** update io.hypersistence:hypersistence-utils-hibernate-73 to v3.15.4 ([#329](https://github.com/ylazakovich/quokkify/issues/329)) ([2e2ca5d](https://github.com/ylazakovich/quokkify/commit/2e2ca5d632498fdaade14c0af5cf8bf1fda81508))
* **deps:** update jira_rest_client_core to v7 ([#146](https://github.com/ylazakovich/quokkify/issues/146)) ([be54c2b](https://github.com/ylazakovich/quokkify/commit/be54c2b0d0f06bc806e83de39d3383de6ab4744a))
* **deps:** update jira_rest_client_core to v7.0.2 ([#324](https://github.com/ylazakovich/quokkify/issues/324)) ([9d0e219](https://github.com/ylazakovich/quokkify/commit/9d0e21983f7e7f9c78269336184c509184302335))
* **deps:** update log4j2 monorepo to v2.25.3 ([#102](https://github.com/ylazakovich/quokkify/issues/102)) ([2d1f8d7](https://github.com/ylazakovich/quokkify/commit/2d1f8d7d1bff881af35cc79010896b4aca022e52))
* **deps:** update log4j2 monorepo to v2.25.4 ([#184](https://github.com/ylazakovich/quokkify/issues/184)) ([1cef1f8](https://github.com/ylazakovich/quokkify/commit/1cef1f8d0b5443237733a6a639800cdf5e1e6fbb))
* **deps:** update log4j2 monorepo to v2.26.0 ([#231](https://github.com/ylazakovich/quokkify/issues/231)) ([45d2224](https://github.com/ylazakovich/quokkify/commit/45d22245d57ce070d26ec9d6ecc866881c256787))
* **deps:** update log4j2 monorepo to v2.26.1 ([#309](https://github.com/ylazakovich/quokkify/issues/309)) ([d914fec](https://github.com/ylazakovich/quokkify/commit/d914fec0aed6cde2ef182556a9259980368ee3d2))
* **deps:** update net.datafaker:datafaker to v2.5.3 ([#61](https://github.com/ylazakovich/quokkify/issues/61)) ([df98e57](https://github.com/ylazakovich/quokkify/commit/df98e571e28ed5406e57d499472c3c8c427be96a))
* **deps:** update net.datafaker:datafaker to v2.5.4 ([#185](https://github.com/ylazakovich/quokkify/issues/185)) ([9eea85c](https://github.com/ylazakovich/quokkify/commit/9eea85c1f4d1086e77a780bd88950d9b9599f68f))
* **deps:** update net.datafaker:datafaker to v2.6.0 ([#301](https://github.com/ylazakovich/quokkify/issues/301)) ([24a4060](https://github.com/ylazakovich/quokkify/commit/24a4060d86584e4fa18d44625fce87d3122f7081))
* **deps:** update net.datafaker:datafaker to v2.7.0 ([#310](https://github.com/ylazakovich/quokkify/issues/310)) ([38fc5ab](https://github.com/ylazakovich/quokkify/commit/38fc5abd8e71eb15c7de4dc7a8261e4cd728503f))
* **deps:** update org.apache.commons:commons-lang3 to v3.20.0 ([#90](https://github.com/ylazakovich/quokkify/issues/90)) ([bee0521](https://github.com/ylazakovich/quokkify/commit/bee0521f0768b8770d2be4456c6dd7578e0edba7))
* **deps:** update org.apache.commons:commons-text to v1.15.0 ([#91](https://github.com/ylazakovich/quokkify/issues/91)) ([101e6f1](https://github.com/ylazakovich/quokkify/commit/101e6f123dcb80219089a92116a3b4bacd554542))
* **deps:** update org.apache.httpcomponents.core5:httpcore5 to v5.4 ([#92](https://github.com/ylazakovich/quokkify/issues/92)) ([7289f34](https://github.com/ylazakovich/quokkify/commit/7289f349192673d977934fec202107dc496f2ace))
* **deps:** update org.apache.kafka:kafka-clients to v4.2.0 ([#192](https://github.com/ylazakovich/quokkify/issues/192)) ([ae2a199](https://github.com/ylazakovich/quokkify/commit/ae2a199f6561ff10a0021499ac1b0d1677529bdf))
* **deps:** update org.apache.kafka:kafka-clients to v4.3.0 ([#263](https://github.com/ylazakovich/quokkify/issues/263)) ([84661c9](https://github.com/ylazakovich/quokkify/commit/84661c91412a0409316bfd19cc386705e5562f3b))
* **deps:** update org.apache.kafka:kafka-clients to v4.3.1 ([#308](https://github.com/ylazakovich/quokkify/issues/308)) ([61c97ef](https://github.com/ylazakovich/quokkify/commit/61c97efac3586448d1c2746c64639439d551008e))
* **deps:** update org.aspectj:aspectjweaver to v1.9.25 ([#62](https://github.com/ylazakovich/quokkify/issues/62)) ([5896be4](https://github.com/ylazakovich/quokkify/commit/5896be4b3ac123621aba58dc1da4082dbdc28501))
* **deps:** update org.aspectj:aspectjweaver to v1.9.25.1 ([#106](https://github.com/ylazakovich/quokkify/issues/106)) ([a811e72](https://github.com/ylazakovich/quokkify/commit/a811e72c5fdd0152d05c414c94b52e4231139a15))
* **deps:** update org.assertj:assertj-core to v3.27.7 ([#123](https://github.com/ylazakovich/quokkify/issues/123)) ([79de5e4](https://github.com/ylazakovich/quokkify/commit/79de5e4ac5d0257da2e02c464f56bab7a6a1a05b))
* **deps:** update org.bouncycastle:bcprov-jdk18on to v1.83 ([#93](https://github.com/ylazakovich/quokkify/issues/93)) ([85db7a2](https://github.com/ylazakovich/quokkify/commit/85db7a2227e7c945320ad7456b7ded9ab4e4da44))
* **deps:** update org.bouncycastle:bcprov-jdk18on to v1.84 ([#193](https://github.com/ylazakovich/quokkify/issues/193)) ([30eac08](https://github.com/ylazakovich/quokkify/commit/30eac08756a7134d6cfeaafe4821eb0a4eefa53e))
* **deps:** update org.jetbrains:annotations to v26.1.0 ([#217](https://github.com/ylazakovich/quokkify/issues/217)) ([18e3717](https://github.com/ylazakovich/quokkify/commit/18e371797a9ab77533b26c3525d1bb3301560b82))
* **deps:** update org.mockito:mockito-core to v5.23.0 ([#244](https://github.com/ylazakovich/quokkify/issues/244)) ([cd7e6d4](https://github.com/ylazakovich/quokkify/commit/cd7e6d48f43ea9b9b76b40fb61c1a1887c714277))
* **deps:** update org.modelmapper:modelmapper to v3.2.6 ([#72](https://github.com/ylazakovich/quokkify/issues/72)) ([8626745](https://github.com/ylazakovich/quokkify/commit/862674575ec865c7bf9e0b386e74d6b6a6e684bd))
* **deps:** update org.mongodb:mongodb-driver-sync to v5.6.3 ([#154](https://github.com/ylazakovich/quokkify/issues/154)) ([ca84cbe](https://github.com/ylazakovich/quokkify/commit/ca84cbe57ab0bae7df0332e8723c6d7231aed7d5))
* **deps:** update org.mongodb:mongodb-driver-sync to v5.7.0 ([#189](https://github.com/ylazakovich/quokkify/issues/189)) ([a62f0de](https://github.com/ylazakovich/quokkify/commit/a62f0de3cbf812c37fd1e1b1e1e4474ce95250e4))
* **deps:** update org.mongodb:mongodb-driver-sync to v5.8.0 ([#294](https://github.com/ylazakovich/quokkify/issues/294)) ([44bab28](https://github.com/ylazakovich/quokkify/commit/44bab2803fdde949242c5027d4b972a574305cc4))
* **deps:** update org.mongodb:mongodb-driver-sync to v5.9.0 ([#326](https://github.com/ylazakovich/quokkify/issues/326)) ([3cf6ee2](https://github.com/ylazakovich/quokkify/commit/3cf6ee295ebf2b8b88853452aa49e1e1842ad50b))
* **deps:** update org.redisson:redisson to v4 ([#160](https://github.com/ylazakovich/quokkify/issues/160)) ([8d8d2fe](https://github.com/ylazakovich/quokkify/commit/8d8d2fe615ca26b8e3f31673d3a356dd8c84d109))
* **deps:** update org.redisson:redisson to v4.3.1 ([#194](https://github.com/ylazakovich/quokkify/issues/194)) ([e7e47b0](https://github.com/ylazakovich/quokkify/commit/e7e47b074d6a3af4c908679a9d3b59e91ff462c5))
* **deps:** update org.redisson:redisson to v4.4.0 ([#264](https://github.com/ylazakovich/quokkify/issues/264)) ([361bc6e](https://github.com/ylazakovich/quokkify/commit/361bc6e0de6a0a1d9e5699cfc24f87715155e678))
* **deps:** update org.redisson:redisson to v4.6.0 ([#296](https://github.com/ylazakovich/quokkify/issues/296)) ([9233504](https://github.com/ylazakovich/quokkify/commit/92335047877c75644f05d5c99d27176ccff7b1dc))
* **deps:** update org.redisson:redisson to v4.6.1 ([#316](https://github.com/ylazakovich/quokkify/issues/316)) ([e740580](https://github.com/ylazakovich/quokkify/commit/e740580707f48d401b49864a259b8fa5e34cdb2a))
* **deps:** update org.testng:testng to v7.12.0 ([#120](https://github.com/ylazakovich/quokkify/issues/120)) ([825b78f](https://github.com/ylazakovich/quokkify/commit/825b78f617f25b5ab079dae01ba2dfece286a780))
* **deps:** update rest_assured to v6 ([#96](https://github.com/ylazakovich/quokkify/issues/96)) ([7928826](https://github.com/ylazakovich/quokkify/commit/7928826d9507703b50079059fec6c29f1138d89a))
* **deps:** update rest_assured to v6.0.1 ([#345](https://github.com/ylazakovich/quokkify/issues/345)) ([ac02887](https://github.com/ylazakovich/quokkify/commit/ac02887ca7c886d46d8f13d2bff72541cc6ace39))
* **deps:** update selenide to v7.14.0 ([#121](https://github.com/ylazakovich/quokkify/issues/121)) ([8d5fdaf](https://github.com/ylazakovich/quokkify/commit/8d5fdaf84a97bdda9bec3b7f8647037996ca52d6))
* **deps:** update selenide to v7.16.0 ([#190](https://github.com/ylazakovich/quokkify/issues/190)) ([04aab0e](https://github.com/ylazakovich/quokkify/commit/04aab0e7a475e2baf346c6c13cf115bfc33009b6))
* **deps:** update selenide to v7.16.1 ([#212](https://github.com/ylazakovich/quokkify/issues/212)) ([b7a71f1](https://github.com/ylazakovich/quokkify/commit/b7a71f17cc9150922199f2e05c0d337e9e36e443))
* **deps:** update selenide to v7.16.2 ([#278](https://github.com/ylazakovich/quokkify/issues/278)) ([3e90a42](https://github.com/ylazakovich/quokkify/commit/3e90a42a6b1c5ade6a43433f7742aa59779c469a))
* **deps:** update slf4j monorepo to v2.0.18 ([#260](https://github.com/ylazakovich/quokkify/issues/260)) ([2c83b6f](https://github.com/ylazakovich/quokkify/commit/2c83b6f61370e469c6c88b306c664372c6db2e05))
* **deps:** update tyrus to v2.2.2 ([#218](https://github.com/ylazakovich/quokkify/issues/218)) ([c09cac3](https://github.com/ylazakovich/quokkify/commit/c09cac367a635a5c2a618e76b9e4331c90f81660))
* revert actions permission to none in detect-runner ([8b4f1f5](https://github.com/ylazakovich/quokkify/commit/8b4f1f52b71d54510678f947848cffc9f9b1f5f7))

## 2026-05-03

- `integrations/tyrus` — added Tyrus WebSocket module with client, steps, verifier, SPI-based TestNG configuration, and integration tests against embedded echo server
- `docs` — rewrote all module READMEs with BaseTest initialization and usage patterns
- `docs` — added README for data-utils/nosql/redis
- `docs` — fixed data-utils/sql README with correct initialization chain and QueryDSL examples
- `docs` — added README for all previously undocumented modules (awaitility, config, console, file, html, introspection, jackson, jwt, signature, morphia, sql, testng-extensions)
- `ci` — added fallback to public GitHub runner when self-hosted runner is offline
- `common-utils/awaitility` — added `assertNeverTrue` and `assertAlwaysTrue` methods to `Waiter`
- `common-utils/file` — added `readAsString(Path)`, `getResourceAsString(String)`, `getResourcePath(String, String)`, and `getDirectoriesAsEnumValuesFromConfiguration` methods
- `integrations/selenide` — added `sendKeys(Double)`, `sendKeys(LocalDate, DateType)`, and `sendKeys(LocalDateTime, DateType)` overloads to `Input`
- `docs` — added SPI-based listener loading section to `testng-extensions` README

## 2026-02-11

- `ci/workflows` — improved GitHub Actions workflow configuration
- `environment` — refactored Docker Compose configuration layering and environment bootstrap scripts
- `integrations/rabbitmq` — enabled RabbitMQ integration module with client, steps, and integration tests

## 2026-02-10

- `integrations/kafka` — enabled Kafka integration module
- `integrations/reportportal` — enabled ReportPortal integration module with TestNG listener and configuration
- `integrations/testrail` — migrated custom HTTP client to Feign
- `ci` — resolved CI issue for self-hosted runner and disabled flaky test on main branch
- `data-utils/nosql/redis` — added new Redis module (Redisson-based) with smoke tests
- `data-utils/nosql/morphia` — refactored MongoDB module structure under morphia submodule

## 2026-02-09

- `ci/runner` — enabled Renovate to trigger CI via self-hosted runner
- `common-utils/jackson` — added auto-discovery for Jackson modules via SPI in JSON, XML, YAML, and CSV converters

## 2026-02-01

- `integrations/testrail` — added TestRail integration module with full API client, TestNG listeners, and Jira ticket source
- `data-utils/nosql` — enabled MongoDB module with Morphia-based entity support and integration tests

## 2026-01-18

- `ci` — hotfix for self-hosted runner type detection
- `ci` — added self-hosted runner preference with safe fallback to GitHub-hosted runner

## 2026-01-01

- `testng-extensions` — created TestNG extension configuration with suite lifecycle listener, wired via SPI across all modules

## 2025-12-16

- `integrations/selenide` — added Selenide module with Page Object component library (buttons, inputs, tables, dropdowns) and browser configuration
- `common-utils/html` — added HTML constants and parser exception utilities

## 2025-12-15

- `ci` — configured concurrent CI jobs with one module per runner for parallel execution
- `ci` — fixed workflow summary generation issue
- `docs` — added Renovate badge to README

## 2025-10-06

- `ci` — enabled Prettier lint check via GitHub Actions workflow

## 2025-10-03

- `data-utils/sql` — replaced HSQL with H2 as in-memory database for SQL module tests

## 2025-10-01

- `common-utils/jackson` — fixed Jackson deprecations across JSON/XML/YAML converters
- `build` — replaced deprecated SpotBugs usages and applied checkstyle fixes

## 2025-09-30

- `build` — initial multi-module Gradle layout, GitHub Actions workflow, checkstyle configuration
- `data-utils/sql` — added SQL module implementation
- `tools/environment` — added local Docker Compose environment with mock-server expectations

## 2025-09-15

- `project` — initial project bootstrap

## 2025-09-12

- `project` — initial commit
