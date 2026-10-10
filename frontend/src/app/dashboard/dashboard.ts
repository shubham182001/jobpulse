import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

import { ToastrService } from 'ngx-toastr';
import { DashboardService } from '../services/dasboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {

  user: any;
  activeTab: 'dashboard' | 'myjobs' | 'trigger' | 'reset' = 'dashboard';

  allJobs: any[] = [];
  myJobs: any[] = [];

  loadingJobs = false;
  loadingMyJobs = false;

  agentQuery = 'find jobs for zoho';
  agentRunning = false;
  agentResult = '';

  constructor(
    private auth: AuthService,
    private dashboardService: DashboardService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadAllJobs();
  }

  setTab(tab: any) {
    this.activeTab = tab;
    if (tab === 'dashboard') this.loadAllJobs();
    if (tab === 'myjobs') this.loadMyJobs();
  }

  loadAllJobs() {
    this.loadingJobs = true;
    this.dashboardService.getAllJobs().subscribe({
      next: (jobs) => {
        this.allJobs = jobs;
        this.loadingJobs = false;
      },
      error: () => {
        this.loadingJobs = false;
        this.toastr.error('Failed to load jobs');
      }
    });
  }

  loadMyJobs() {
    this.loadingMyJobs = true;
    const userId = this.user.user?.id;
    this.dashboardService.getMyJobs(userId).subscribe({
      next: (jobs) => {
        this.myJobs = jobs;
        this.loadingMyJobs = false;
      },
      error: () => {
        this.loadingMyJobs = false;
        this.toastr.error('Failed to load your jobs');
      }
    });
  }

  triggerAgent() {
    this.agentRunning = true;
    this.agentResult = '';

    this.dashboardService.runAgent(this.agentQuery).subscribe({
      next: (res) => {
        this.agentResult = res.result;
        this.agentRunning = false;
        this.toastr.success('Agent completed!');
        this.loadAllJobs();
      },
      error: (err) => {
        this.agentRunning = false;
        this.toastr.error(err.error?.error || 'Agent failed');
      }
    });
  }

  resetPreferences() {
    if (!confirm('Are you sure? This will clear all your preferences.')) return;

    const userId = this.user.user?.id;
    this.dashboardService.resetPreferences(userId).subscribe({
      next: () => {
        this.toastr.success('Preferences reset successfully');
      },
      error: () => {
        this.toastr.error('Failed to reset preferences');
      }
    });
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/auth']);
  }
}