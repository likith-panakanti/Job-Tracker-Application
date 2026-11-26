import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JobService } from './job.service';
import { COLUMNS, JobApplication, Status } from './models';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private api = inject(JobService);

  readonly columns = COLUMNS;
  readonly jobs = signal<JobApplication[]>([]);
  readonly error = signal('');
  readonly total = computed(() => this.jobs().length);
  readonly active = computed(() =>
    this.jobs().filter((j) => j.status === 'APPLIED' || j.status === 'INTERVIEW').length);

  draft: JobApplication = this.emptyDraft();

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.api.list().subscribe({
      next: (jobs) => { this.jobs.set(jobs); this.error.set(''); },
      error: () => this.error.set('Cannot reach the API. Start the backend on port 8080, then refresh.')
    });
  }

  inColumn(status: Status): JobApplication[] {
    return this.jobs().filter((j) => j.status === status);
  }

  add(): void {
    if (!this.draft.company.trim() || !this.draft.role.trim()) return;
    this.api.create(this.draft).subscribe({
      next: (job) => { this.jobs.update((list) => [job, ...list]); this.draft = this.emptyDraft(); },
      error: () => this.error.set('Could not save the application.')
    });
  }

  move(job: JobApplication, status: Status): void {
    this.api.updateStatus(job.id!, status).subscribe((updated) =>
      this.jobs.update((list) => list.map((j) => (j.id === updated.id ? updated : j))));
  }

  remove(job: JobApplication): void {
    this.api.remove(job.id!).subscribe(() =>
      this.jobs.update((list) => list.filter((j) => j.id !== job.id)));
  }

  private emptyDraft(): JobApplication {
    return { company: '', role: '', location: '', link: '', status: 'WISHLIST' };
  }
}
