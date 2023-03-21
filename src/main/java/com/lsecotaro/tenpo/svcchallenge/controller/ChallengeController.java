package com.lsecotaro.tenpo.svcchallenge.controller;

import com.lsecotaro.tenpo.svcchallenge.model.RequestHistoricalResponse;
import com.lsecotaro.tenpo.svcchallenge.model.SumRequest;
import com.lsecotaro.tenpo.svcchallenge.model.SumResponse;
import com.lsecotaro.tenpo.svcchallenge.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api-rest")
@Slf4j
public class ChallengeController {
    private final ChallengeService challengeService;


    @Autowired
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @Operation(summary = "Do this calculation: sum all passed valuesToOperate and apply a percentage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculation ok",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SumResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Percentage value not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Percentage service not available",
            content = @Content),
            @ApiResponse(responseCode = "429", description = "Rate Limit, too many request",
                    content = @Content) })
    @PostMapping(value = "/calc")
    @ResponseStatus(value = HttpStatus.OK)
    public SumResponse calc(@RequestBody SumRequest request) {
        log.info("calc");
        return challengeService.calc(request.getValuesToOperate());
    }

    @Operation(summary = "Get paginated requests historical")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Information ok",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestHistoricalResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content)})
    @GetMapping(value = "/request/historical")
    @ResponseStatus(value = HttpStatus.OK)
    public RequestHistoricalResponse historical(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return challengeService.getHistorical(page, limit);
    }
}
