import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobApplication, Status } from './models';

const API = 'http://localhost:8080/api/applications';

@Injectable({ providedIn: 'root' })
export class JobService {
  private http = inject(HttpClient);

  list(): Observable<JobApplication[]> {
    return this.http.get<JobApplication[]>(API);
  }

  create(job: JobApplication): Observable<JobApplication> {
    return this.http.post<JobApplication>(API, job);
  }

  updateStatus(id: number, status: Status): Observable<JobApplication> {
    return this.http.patch<JobApplication>(`${API}/${id}/status`, { status });
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/${id}`);
  }
}
