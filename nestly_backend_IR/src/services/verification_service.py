import asyncio
from src.db.session import session_local
from src.repositories import properties_repo
from src.models.report import VerificationReport, ReportStatus
from src.services.reviewers_service import review_completeness, review_consistency, review_pricing, review_red_flags
from src.services.consolidate_service import consolidate


async def run_verification_panel_task(property_id):
    db = session_local()
    try:
        prop = properties_repo.get_property_by_id(db, property_id)
        comparables = properties_repo.get_comparable_rents(db, prop.location, exclude_id=prop.id)
        
        listing_json = str({
            "title": prop.title,
            "description": prop.description,
            "location": prop.location,
            "rent_amount": prop.rent_amount,
            "amenities": prop.amenities,
            "photo_count": len(prop.media),
            "comparable_rents": comparables,
        })
    
        completeness, consistency, pricing, red_flags = await asyncio.gather(
            review_completeness(listing_json),
            review_consistency(listing_json),
            review_pricing(listing_json),
            review_red_flags(listing_json),
        )

        final = await consolidate(completeness, consistency, pricing, red_flags)

        report = VerificationReport(
            property_id=property_id,
            completeness=completeness.model_dump(),
            consistency=consistency.model_dump(),
            pricing=pricing.model_dump(),
            red_flags=red_flags.model_dump(),
            summary=final.summary,
            key_concerns=final.key_concerns,
            recommendation=final.recommendation,
            status=ReportStatus.COMPLETED,
        )
        db.add(report)
        db.commit()

    finally:
        db.close()
                
def run_verification_panel_task_sync(property_id):
    asyncio.run(run_verification_panel_task(property_id))

