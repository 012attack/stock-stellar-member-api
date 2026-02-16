1. 아래처럼 본인 domain의 query 구현체가 아닌데 이렇게 직접적으로 사용하지 않았으면 좋겠어요.
   val member = memberRepository.findById(memberId)
   .orElseThrow { IllegalArgumentException("Member not found: $memberId") }

이유: 특정기술에 의존적이게 되고,
   .orElseThrow { IllegalArgumentException("Member not found: $memberId") } 이런 코드가 중복될 가능성이 커요

1-1 아래처럼 인터페이스를 사용해주세요
   @Service
   @Transactional
   class CommandStockGroupCreator(
   private val stockGroupRepository: StockGroupRepository,
   private val memberRepository: MemberRepository,
   private val memberFinder: MemberFinder
   ) : StockGroupCreator {

   override fun create(request: CreateStockGroupRequest, memberId: Long): StockGroupResponse {
   val member = memberFinder.findById(memberId)

