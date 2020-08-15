import { Injectable } from '@angular/core';

import { ReportElement, ReportResponse, ErrorResponse, ErrorType, SuccessResponse } from '../app/shared/report_req_res';

@Injectable()
export class UploadTestReport {

    static validFileName: string = "../../test/records.xml";
    static emptyFileName: string = "../../test/empty-records.xml";
    static txtFileName: string = "../../test/txt-file.txt";

    static validFile: File = new File(['records'], UploadTestReport.validFileName, { type: 'text/xml' });
    static emptyFile: File = new File([''], UploadTestReport.emptyFileName, { type: 'text/xml' });
    static txtFile: File = new File(['Have some text'], UploadTestReport.txtFileName);

    error: ErrorType = {
    };

    successResponse: SuccessResponse = {};

    errorRespnse: ErrorResponse = {
        success: false,
        errorMessage: "",
        remarks: ""
    };

    reportResponse: ReportResponse = {
        remarks: "",
        reports: [],
        success: true
    };

    errorDataReport: ReportElement[] = [
        {
            "transactionReference": "167875",
            "accountNumber": "NL93ABNA0585619023",
            "description": "Tickets from Erik de Vries",
            "remarks": "Error record and Duplicate Reference"
        },
        {
            "transactionReference": "183049",
            "accountNumber": "NL69ABNA0433647324",
            "description": "Clothes for Jan King",
            "remarks": "Error record"
        },
        {
            "transactionReference": "167875",
            "accountNumber": "NL93ABNA0585619023",
            "description": "Tickets from Richard Bakker",
            "remarks": "Duplicate Reference"
        }
    ];


}